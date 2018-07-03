package io.ankburov.universalconverter.service.validation

import io.ankburov.universalconverter.model.Schema

interface SchemaValidationService {

    fun validateDocument(documents: List<String>, schema: Schema, properties: Map<String, String> = emptyMap())
}