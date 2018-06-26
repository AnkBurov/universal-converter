package io.ankburov.kotlinxsltdsl.service

import io.ankburov.kotlinxsltdsl.model.Schema
import java.util.UUID

interface TransformationService {

    fun transformFile(schema: Schema, fileUuid: UUID, mappings: Map<String, String>): List<String>
}