package io.ankburov.universalconverter.unit

import io.ankburov.universalconverter.service.validation.SchemaValidationStrategy
import io.ankburov.universalconverter.service.validation.xml.XmlExampleValidationStrategy
import io.ankburov.universalconverter.utils.TestConstants.Companion.EXPECTED_XML
import io.ankburov.universalconverter.utils.TestConstants.Companion.INVALID_XML
import org.junit.Test

class XmlExampleValidationStrategyTest {

    private val validationStrategy: SchemaValidationStrategy = XmlExampleValidationStrategy()

    @Test
    fun validateXml() {
        val sourceXml = EXPECTED_XML

        validationStrategy.validateDocument(listOf(sourceXml))
    }

    @Test(expected = IllegalArgumentException::class)
    fun validateInvalidXml() {
        val sourceXml = INVALID_XML

        validationStrategy.validateDocument(listOf(sourceXml))
    }
}