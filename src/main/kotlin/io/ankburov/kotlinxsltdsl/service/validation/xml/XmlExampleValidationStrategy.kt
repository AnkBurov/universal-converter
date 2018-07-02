package io.ankburov.kotlinxsltdsl.service.validation.xml

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.service.validation.SchemaValidationStrategy
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component
import org.xml.sax.SAXException
import javax.xml.transform.stream.StreamSource
import javax.xml.validation.SchemaFactory

@Component
class XmlExampleValidationStrategy : SchemaValidationStrategy {

    private val xsdSchemaFile = ClassPathResource("schema/validation/${getSchema().schemaName}").file

    override fun validateDocument(documents: List<String>, properties: Map<String, String>) {
        require(documents.isNotEmpty()) { "No xmls to validate" }

        val xml = documents[0]

        val factory = SchemaFactory.newInstance("http://www.w3.org/2001/XMLSchema")
        val schema = factory.newSchema(xsdSchemaFile)
        val validator = schema.newValidator()
        xml.byteInputStream().use {
            try {
                validator.validate(StreamSource(it))
            } catch (e: SAXException) {
                throw IllegalArgumentException("XML is not valid because: ${e.message}", e)
            }
        }
    }

    final override fun getSchema() = Schema.XML_EXAMPLE_V1
}