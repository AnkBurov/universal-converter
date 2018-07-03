package io.ankburov.universalconverter.service.dst

import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.model.Tree

interface DestinationConverterService {

    /**
     * @return up to implementation combine all trees into single document or split them
     */
    fun convertTrees(trees: List<Tree>, schema: Schema): List<String>

    fun getConverterStrategy(schema: Schema): ConverterStrategy

    fun getAvailableSchemas(): List<Schema>
}