package io.ankburov.universalconverter.service.dst

import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.model.Tree
import org.springframework.stereotype.Service

@Service
class DestinationConverterServiceImpl(
        private val converterStrategies: List<ConverterStrategy>
) : DestinationConverterService {
    override fun convertTrees(trees: List<Tree>, schema: Schema): List<String> {
        val converterStrategy = converterStrategies.firstOrNull { it.getSchema() == schema }
                ?: throw IllegalArgumentException("Schema ${schema.name} is unsupported")

        return converterStrategy.convertTrees(trees)
    }

    override fun getConverterStrategy(schema: Schema): ConverterStrategy {
        return converterStrategies.firstOrNull { it.getSchema() == schema }
                ?: throw IllegalArgumentException("Schema ${schema.name} is unsupported")
    }

    override fun getAvailableSchemas() = converterStrategies.map(ConverterStrategy::getSchema)
}
