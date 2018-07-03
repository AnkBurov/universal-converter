package io.ankburov.universalconverter.service.validation

import io.ankburov.universalconverter.model.Schema

interface SchemaValidationStrategy {

    /**
     * Validate generated document against schema
     *
     * up to implementation combine all documents into single document or split them
     */
    fun validateDocument(documents: List<String>, properties: Map<String, String> = emptyMap())

    fun getSchema(): Schema
}