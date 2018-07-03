package io.ankburov.universalconverter.service

import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.service.dst.DestinationConverterService
import io.ankburov.universalconverter.service.file.FilePersistenceService
import io.ankburov.universalconverter.service.src.SourceFileParserTreeService
import io.ankburov.universalconverter.service.validation.SchemaValidationService
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class TransformationServiceImpl(
        private val filePersistenceService: FilePersistenceService,
        private val sourceFileParserService: SourceFileParserTreeService,
        private val destinationConverterService: DestinationConverterService,
        private val schemaValidationService: SchemaValidationService
) : TransformationService {

    override fun transformFile(schema: Schema, fileUuid: UUID, mappings: Map<String, String>, properties: Map<String, String>): List<String> {
        val fileReader = filePersistenceService.getFileReader(fileUuid)
        val trees = sourceFileParserService.parseFile(fileReader, mappings, properties)
        return destinationConverterService.convertTrees(trees, schema)
                .also { schemaValidationService.validateDocument(it, schema, properties) }
    }
}