package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.service.dst.xml.XmlExampleConverterStrategy
import io.ankburov.kotlinxsltdsl.service.src.csv.CsvFileParserTreeService
import io.ankburov.kotlinxsltdsl.utils.Constants
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

    companion object {
        val EXPECTED_XML = """<?xml version="1.0" encoding="UTF-8"?>
<root xmlns="some">
	<cars>
		<year>
			1997
		</year>
		<brand>
			Ford
		</brand>
		<model>
			E350
		</model>
		<something isWhatever="sure">
			<nested>
				nested
			</nested>
		</something>
	</cars>
	<cars>
		<year>
			2012
		</year>
		<brand>
			Tesla
		</brand>
		<model>
			Model S
		</model>
		<something isWhatever="sure">
			<nested>
				nested
			</nested>
		</something>
	</cars>
</root>""".trimIndent()
    }
}