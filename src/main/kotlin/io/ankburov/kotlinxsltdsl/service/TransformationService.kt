package io.ankburov.kotlinxsltdsl.service

import io.ankburov.kotlinxsltdsl.model.Schema
import java.nio.file.Path

interface TransformationService {

    fun transformFile(file: Path, schema: Schema, mappings: Map<String, String>): List<String>
}