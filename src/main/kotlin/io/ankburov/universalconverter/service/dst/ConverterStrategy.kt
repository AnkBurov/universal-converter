package io.ankburov.universalconverter.service.dst

import io.ankburov.universalconverter.model.NodeName
import io.ankburov.universalconverter.model.Schema
import io.ankburov.universalconverter.model.Tree

interface ConverterStrategy {

    /**
     * @return up to implementation combine all trees into single document or split them
     */
    fun convertTrees(trees: List<Tree>): List<String>

    fun getSchema(): Schema

    fun getUsingNodeNames(): List<NodeName>
}