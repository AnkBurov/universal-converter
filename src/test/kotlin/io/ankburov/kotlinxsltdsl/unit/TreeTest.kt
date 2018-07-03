package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.model.NodeName
import io.ankburov.kotlinxsltdsl.model.Tree
import io.ankburov.kotlinxsltdsl.model.treeDsl
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class TreeTest {

    @Test
    fun test() {
        val tree = Tree.new()

        val keysToValues = listOf("whatever" to "first",
                "whatever/second" to "second",
                "whatever/third" to "third",
                "whatever/third/inner" to "innerValue",
                "whatever/third/inner/sub" to "innerValue",
                "whatever/third/inner/sub2" to "innerValue",
                "whatever/change" to "oldValue",
                "whatever/change" to "newValue",
                "another/fourth" to "fourth")

        keysToValues.forEach { tree.addChild(it.first, it.second) }

        keysToValues
                .filterNot { it.first == "whatever/change" && it.second == "oldValue" }
                .forEach { assertEquals(it.second, tree.getValue(it.first)) }
    }

    @Test
    fun testDsl() {
        val tree = treeDsl {
            "node"("1") {
                "inner"("fdafadf")
            }
            "node2"("2")
        }

        assertEquals("1", tree.getValue("node"))
        assertEquals("fdafadf", tree.getValue("node/inner"))
        assertEquals("2", tree.getValue("node2"))
    }

    @Test
    fun testIterable() {
        val values = listOf("1", "fdafadf", "2")

        val tree = treeDsl {
            "node"(values[0]) {
                "inner"(values[1])
            }
            "node2"(values[2])
        }

        tree.map { it.value }
                .forEach {
                    assertTrue(values.contains(it))
                }
    }

    @Test(expected = IllegalArgumentException::class)
    fun testAbsentValue() {
        val nodeName = NodeName("some/absent/node", isRequired = true)

        val xml = treeDsl {
            "node"("1") {
                "inner"("fdafadf")
            }
            "node2"("2")
        }

        xml.getValue(nodeName)
    }
}