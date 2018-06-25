package io.ankburov.kotlinxsltdsl.service.dst

import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.model.Tree

interface ConverterStrategy {

    /**
     * @return up to implementation combine all trees into single document or split them
     */
    fun convertTrees(trees: List<Tree>): List<String>

    fun getSchema(): Schema

    fun getUsingNodeNames(): List<String>
}