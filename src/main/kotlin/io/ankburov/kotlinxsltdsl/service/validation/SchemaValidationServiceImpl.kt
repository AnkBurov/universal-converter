package io.ankburov.kotlinxsltdsl.service.validation

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.utils.Constants.Companion.SKIP_VALIDATION
import org.springframework.stereotype.Service

@Service
class SchemaValidationServiceImpl(
        private val validationStrategies: List<SchemaValidationStrategy>
) : SchemaValidationService {
    override fun validateDocument(documents: List<String>, schema: Schema, properties: Map<String, String>) {
        if (skipValidation(properties)) {
            return
        }
        val validationStrategy = (validationStrategies.firstOrNull { it.getSchema() == schema }
                ?: throw IllegalArgumentException("Schema ${schema.name} is unsupported"))

        return validationStrategy.validateDocument(documents, properties)
    }

    private fun skipValidation(properties: Map<String, String>) = properties[SKIP_VALIDATION]?.equals("true", ignoreCase = true) == true
}