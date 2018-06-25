package io.ankburov.kotlinxsltdsl.service.src

import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.service.TransformationService
import java.nio.file.Path

interface SourceFileParserTreeService {

    fun parseFile(path: Path, mappings: Map<String, String>): List<Tree>
}