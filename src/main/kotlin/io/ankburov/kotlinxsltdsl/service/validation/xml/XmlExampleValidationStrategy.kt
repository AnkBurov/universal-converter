package io.ankburov.kotlinxsltdsl.service.validation.xml

import io.ankburov.kotlinxsltdsl.model.Schema
import org.springframework.core.io.ClassPathResource
import org.springframework.stereotype.Component

@Component
class XmlExampleValidationStrategy : SingleXmlValidationStrategy() {

    private val xsdSchemaFile = ClassPathResource("schema/validation/${getSchema().schemaName}").file

    override fun getXsdSchemaFile() = xsdSchemaFile

    final override fun getSchema() = Schema.XML_EXAMPLE_V1
}