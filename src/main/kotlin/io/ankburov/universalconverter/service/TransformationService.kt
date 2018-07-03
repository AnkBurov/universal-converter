package io.ankburov.universalconverter.service

import io.ankburov.universalconverter.model.Schema
import java.util.UUID

interface TransformationService {

    fun transformFile(schema: Schema, fileUuid: UUID, mappings: Map<String, String>, properties: Map<String, String>): List<String>
}