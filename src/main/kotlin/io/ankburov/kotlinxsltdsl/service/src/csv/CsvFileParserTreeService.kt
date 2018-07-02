package io.ankburov.kotlinxsltdsl.service.src.csv

import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.service.src.SourceFileParserTreeService
import io.ankburov.kotlinxsltdsl.utils.Constants.Companion.IS_FIRST_ROW_A_HEADER
import io.ankburov.kotlinxsltdsl.utils.Constants.Companion.IS_NUMERIC
import io.ankburov.kotlinxsltdsl.utils.absolutePath
import org.apache.commons.csv.CSVFormat
import org.apache.commons.csv.CSVRecord
import org.springframework.stereotype.Service
import java.io.FileReader
import java.io.Reader
import java.nio.file.Path

/**
 * Currently the only child
 */
@Service
class CsvFileParserTreeService : SourceFileParserTreeService {

    override fun parseFile(path: Path, mappings: Map<String, String>, properties: Map<String, String>): List<Tree> {
        val reader = FileReader(path.absolutePath())
        return parseFile(reader, mappings, properties)
    }

    override fun parseFile(reader: Reader, mappings: Map<String, String>, properties: Map<String, String>): List<Tree> {
        when {
            isFirstRowAHeader(properties) -> CSVFormat.DEFAULT.withFirstRecordAsHeader()
            else -> CSVFormat.DEFAULT
        }.parse(reader).use { records ->

            val trees = mutableListOf<Tree>()

            records.forEach { record ->
                val tree = Tree.new()

                for (entry in mappings) {
                    if (entry.key == IS_FIRST_ROW_A_HEADER) continue

                    val keyMapping = entry.key
                    val treeNodePlace = entry.value

                    val columnValue = getColumnValue(keyMapping, record).trim()

                    tree.addChild(treeNodePlace, columnValue)
                }

                trees.add(tree)
            }

            return trees
        }
    }

    override fun parseHeaders(path: Path): List<String> {
        val reader = FileReader(path.absolutePath())

        CSVFormat.DEFAULT.parse(reader).use { records ->
            val record = records.firstOrNull() ?: throw IllegalArgumentException("CSV file is empty")
            return record.toCollection(mutableListOf())
        }
    }


    private fun isFirstRowAHeader(mappings: Map<String, String>): Boolean {
        return mappings.get(IS_FIRST_ROW_A_HEADER)?.equals("true", ignoreCase = true) == true
    }

    private fun getColumnValue(columnName: String, record: CSVRecord): String {
        return try {
            when {
                columnName.matches(Regex(IS_NUMERIC)) -> record.get(columnName.toInt())
                else -> record.get(columnName)
            }
        } catch (e: IndexOutOfBoundsException) {
            throw IllegalArgumentException("CSV record doesn't contain column with index $columnName")
        }
    }
}