package io.ankburov.universalconverter.unit

import io.ankburov.universalconverter.dsl.testConverter
import io.ankburov.universalconverter.model.treeDsl
import io.ankburov.universalconverter.service.dst.xml.XmlExampleConverterStrategy
import io.ankburov.universalconverter.service.validation.xml.XmlExampleValidationStrategy
import org.junit.Test

class XmlExampleConverterStrategyTest {

    @Test
    fun testConversion() {

        testConverter {
            +treeDsl {
                "year"("2012")

                "brand"("Tesla")

                "Model S"("Tesla")

                "something" {
                    "nested"("nested")
                }
            }
            converterStrategy = XmlExampleConverterStrategy()
            schemaValidationStrategy = XmlExampleValidationStrategy()
        }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testDslAbsentValue() {

        testConverter {
            +treeDsl {
                "brand"("Tesla")

                "Model S"("Tesla")

                "something" {
                    "nested"("nested")
                }
            }

            +treeDsl {
                "year"("2012")

                "brand"("field")

                "Model S"("Tesla")

                "something" {
                    "nested"("nested")
                }
            }

            converterStrategy = XmlExampleConverterStrategy()
            schemaValidationStrategy = XmlExampleValidationStrategy()
        }
    }
}