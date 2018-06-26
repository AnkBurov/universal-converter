package io.ankburov.kotlinxsltdsl.service.src

import io.ankburov.kotlinxsltdsl.model.Tree
import java.nio.file.Path

interface SourceFileParserTreeService {

    fun parseFile(path: Path, mappings: Map<String, String>): List<Tree>

    fun parseHeaders(path: Path): List<String>
}