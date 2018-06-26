package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.service.dst.xml.XmlExampleConverterStrategy
import io.ankburov.kotlinxsltdsl.service.src.csv.CsvFileParserTreeService
import io.ankburov.kotlinxsltdsl.utils.Constants
import io.ankburov.kotlinxsltdsl.utils.TestConstants.Companion.EXPECTED_XML
import org.junit.Assert.assertEquals
import org.junit.Test
import org.springframework.core.io.ClassPathResource

class XmlExampleConverterServiceTest {

    private val csvFileParserTreeService = CsvFileParserTreeService()

    private val xmlExampleConverterService = XmlExampleConverterStrategy()

    private val csvFile = ClassPathResource("samples/nested.csv")

    //todo remove
    @Test
    fun sortOfIntegrationTest() {
        val mappings = mapOf("year" to "year",
                "brand" to "brand",
                "model" to "model",
                "something/nested" to "something/nested",
                Constants.IS_FIRST_ROW_A_HEADER to "true")

        val trees = csvFileParserTreeService.parseFile(csvFile.file.toPath(), mappings)

        val xmls = xmlExampleConverterService.convertTrees(trees)

        assertEquals(1, xmls.size)
        assertEquals(EXPECTED_XML, xmls[0])
    }
}