package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.service.src.csv.CsvFileParserTreeService
import io.ankburov.kotlinxsltdsl.utils.Constants.Companion.IS_FIRST_ROW_A_HEADER
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.core.io.ClassPathResource

class CsvFileParserTreeServiceTest {

    private val csvFileParserTreeService = CsvFileParserTreeService()

    @Test
    fun testCsvWithHeader() {
        val csv = ClassPathResource("samples/nested.csv")

        val mappings = mapOf("year" to "year",
                "brand" to "brand",
                "model" to "model",
                "something/nested" to "something/nested",
                IS_FIRST_ROW_A_HEADER to "true")

        val trees = csvFileParserTreeService.parseFile(csv.file.toPath(), mappings)

        checkForest(trees)
    }

    @Test
    fun testCsvWithoutHeader() {
        val csv = ClassPathResource("samples/nested_index.csv")

        val mappings = mapOf("0" to "year",
                "1" to "brand",
                "2" to "model",
                "3" to "something/nested")

        val trees = csvFileParserTreeService.parseFile(csv.file.toPath(), mappings)

        checkForest(trees)
    }

    private fun checkForest(trees: List<Tree>) {
        assertEquals(2, trees.size)
        val firstTree = trees[0]
        assertEquals("1997", firstTree.getValue("year"))
        assertEquals("Ford", firstTree.getValue("brand"))
        assertEquals("E350", firstTree.getValue("model"))
        assertEquals("nested", firstTree.getValue("something/nested"))

        val secondTreeTree = trees[1]
        assertEquals("2012", secondTreeTree.getValue("year"))
        assertEquals("Tesla", secondTreeTree.getValue("brand"))
        assertEquals("Model S", secondTreeTree.getValue("model"))
        assertEquals("nested", secondTreeTree.getValue("something/nested"))
    }
}