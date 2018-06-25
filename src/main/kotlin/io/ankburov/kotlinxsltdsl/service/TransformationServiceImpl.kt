package io.ankburov.kotlinxsltdsl.service

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.service.dst.DestinationConverterService
import io.ankburov.kotlinxsltdsl.service.src.SourceFileParserTreeService
import org.springframework.stereotype.Service
import java.nio.file.Path

@Service
class TransformationServiceImpl(
        private val sourceFileParserService: SourceFileParserTreeService,
        private val destinationConverterService: DestinationConverterService
) : TransformationService {

    //todo XSD validation
    override fun transformFile(file: Path, schema: Schema, mappings: Map<String, String>): List<String> {
        val trees = sourceFileParserService.parseFile(file, mappings)
        return destinationConverterService.convertTrees(trees, schema)
    }
}