package io.ankburov.kotlinxsltdsl.dsl

import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.service.dst.ConverterStrategy
import io.ankburov.kotlinxsltdsl.service.validation.SchemaValidationStrategy

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