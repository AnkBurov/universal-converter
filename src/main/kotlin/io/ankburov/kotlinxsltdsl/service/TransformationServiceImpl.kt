package io.ankburov.kotlinxsltdsl.service

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.service.dst.DestinationConverterService
import io.ankburov.kotlinxsltdsl.service.file.FilePersistenceService
import io.ankburov.kotlinxsltdsl.service.src.SourceFileParserTreeService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransformationServiceImpl(
        private val filePersistenceService: FilePersistenceService,
        private val sourceFileParserService: SourceFileParserTreeService,
        private val destinationConverterService: DestinationConverterService
) : TransformationService {

    //todo XSD validation
    override fun transformFile(schema: Schema, fileUuid: UUID, mappings: Map<String, String>): List<String> {
        val fileReader = filePersistenceService.getFileReader(fileUuid)
        val trees = sourceFileParserService.parseFile(fileReader, mappings)
        return destinationConverterService.convertTrees(trees, schema)
    }
}