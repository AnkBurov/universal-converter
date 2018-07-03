package io.ankburov.kotlinxsltdsl.service.validation.xml

import io.ankburov.kotlinxsltdsl.service.validation.SchemaValidationStrategy
import io.ankburov.kotlinxsltdsl.utils.Constants
import org.xml.sax.SAXException
import java.io.File
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

abstract class SingleXmlValidationStrategy : SchemaValidationStrategy {

    override fun validateDocument(documents: List<String>, properties: Map<String, String>) {
        require(documents.isNotEmpty()) { "No xmls to validate" }

        val xml = documents[0]

        val factory = SchemaFactory.newInstance(Constants.W3_XML_SCHEMA)
        val schema = factory.newSchema(getXsdSchemaFile())
        val validator = schema.newValidator()
        xml.byteInputStream().use {
            try {
                validator.validate(StreamSource(it))
            } catch (e: SAXException) {
                throw IllegalArgumentException("XML is not valid because: ${e.message}", e)
            }
        }
    }

    protected abstract fun getXsdSchemaFile(): File
}