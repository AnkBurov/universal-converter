package io.ankburov.kotlinxsltdsl.service.validation

import io.ankburov.kotlinxsltdsl.model.Schema

interface SchemaValidationService {

    fun validateDocument(documents: List<String>, schema: Schema, properties: Map<String, String> = emptyMap())
}