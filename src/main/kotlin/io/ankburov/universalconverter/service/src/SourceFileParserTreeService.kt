package io.ankburov.universalconverter.service.src

import io.ankburov.universalconverter.model.Tree
import java.io.Reader
import java.nio.file.Path

interface SourceFileParserTreeService {

    fun parseFile(path: Path, mappings: Map<String, String>, properties: Map<String, String>): List<Tree>

    fun parseFile(reader: Reader, mappings: Map<String, String>, properties: Map<String, String>): List<Tree>

    fun parseHeaders(path: Path): List<String>
}