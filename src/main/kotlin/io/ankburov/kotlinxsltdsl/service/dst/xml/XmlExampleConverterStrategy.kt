package io.ankburov.kotlinxsltdsl.service.dst.xml

import io.ankburov.kotlinxsltdsl.model.NodeName
import io.ankburov.kotlinxsltdsl.model.Schema
import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.service.dst.ConverterStrategy
import io.ankburov.kotlinxsltdsl.utils.Constants.Companion.XML_HEADER
import io.ankburov.kotlinxsltdsl.utils.getNodeNameFields
import org.redundent.kotlin.xml.xml
import org.springframework.stereotype.Component

@Component
class XmlExampleConverterStrategy : ConverterStrategy {

    override fun convertTrees(trees: List<Tree>): List<String> {
        val xmlBody = xml("root") {
            xmlns = XMLNS

            for (tree in trees) {
                "cars" {
                    "year" {
                        -tree.getValue(YEAR).toString()
                    }
                    "brand" {
                        -tree.getValue(BRAND).toString()
                    }
                    "model" {
                        -tree.getValue(MODEL).toString()
                    }

                    "something" {
                        attribute("isWhatever", "sure")
                        "nested" {
                            -tree.getValue(SOMETHING_NESTED).toString()
                        }
                    }
                }
            }
        }

        return listOf(XML_HEADER + System.lineSeparator() + xmlBody.toString())
    }

    override fun getUsingNodeNames() = usingNodeNamesField

    override fun getSchema() = Schema.XML_EXAMPLE_V1

    companion object {
        const val XMLNS = "some"

        val YEAR = NodeName("year", isRequired = true)
        val BRAND = NodeName("brand")
        val MODEL = NodeName("model")
        val SOMETHING_NESTED = NodeName("something/nested")

        val usingNodeNamesField = this.getNodeNameFields()
    }
}
