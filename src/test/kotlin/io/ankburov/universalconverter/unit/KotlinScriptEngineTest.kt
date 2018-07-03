package io.ankburov.universalconverter.unit

import io.ankburov.universalconverter.service.src.csv.CsvFileParserTreeService
import io.ankburov.universalconverter.utils.Constants
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.redundent.kotlin.xml.Node
import org.springframework.core.io.ClassPathResource
import javax.script.ScriptEngineManager
import javax.script.SimpleBindings

class KotlinScriptEngineTest {

    private val csvFileParserTreeService = CsvFileParserTreeService()

    private val csvFile = ClassPathResource("samples/nested.csv")

    @Test
    fun testSimple() {
        val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")
        val simpleBindings = SimpleBindings()

        with(scriptEngine) {
            simpleBindings.put("someObject", 5)
            val result = eval("""bindings["someObject"] as Int + 5""", simpleBindings)
            assertEquals(10, result)
        }
    }

    @Test
    fun testXmlDsl() {
        // to have created trees object
        val mappings = mapOf("year" to "year",
                "brand" to "brand",
                "model" to "model",
                "something/nested" to "something/nested")

        val properties = mapOf(Constants.IS_FIRST_ROW_A_HEADER to "true")

        val trees = csvFileParserTreeService.parseFile(csvFile.file.toPath(), mappings, properties)

        val scriptEngine = ScriptEngineManager().getEngineByExtension("kts")

        val simpleBindings = SimpleBindings().also {
            it.put("trees", trees)
        }

        val script = """org.redundent.kotlin.xml.xml("root") {
            xmlns = "niceXmlns"

            for (tree in bindings["trees"] as List<io.ankburov.universalconverter.model.Tree>) {
                "cars" {
                    "year" {
                        -tree.getValue("year").toString()
                    }
                    "brand" {
                        -tree.getValue("brand").toString()
                    }
                    "model" {
                        -tree.getValue("model").toString()
                    }

                    "something" {
                        attribute("isWhatever", "sure")
                        "nested" {
                            -tree.getValue("something/nested").toString()
                        }
                    }
                }
            }
        }""".trimIndent()

        val result : Node = scriptEngine.eval(script, simpleBindings) as Node

        assertNotNull(result)
        assertNotNull(result.toString())
        println(result)
    }
}