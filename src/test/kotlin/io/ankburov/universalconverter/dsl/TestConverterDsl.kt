package io.ankburov.universalconverter.dsl

import io.ankburov.universalconverter.model.Tree
import io.ankburov.universalconverter.service.dst.ConverterStrategy
import io.ankburov.universalconverter.service.validation.SchemaValidationStrategy

object testConverter {

    operator fun invoke(init: TestConverterContext.() -> Unit) {
        val converterContext = TestConverterContext()
        converterContext.init()
        converterContext.testConversion()
        return
    }
}

class TestConverterContext {

    var trees = mutableListOf<Tree>()

    lateinit var converterStrategy: ConverterStrategy

    lateinit var schemaValidationStrategy: SchemaValidationStrategy

    var properties: Map<String, String> = mutableMapOf()

    fun testConversion(): List<String> {
        val xmls = converterStrategy.convertTrees(trees)
        schemaValidationStrategy.validateDocument(xmls, properties)
        return xmls
    }

    operator fun Tree.unaryPlus() = trees.add(this)
}