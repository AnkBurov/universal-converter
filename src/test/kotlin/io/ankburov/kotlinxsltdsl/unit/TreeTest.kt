package io.ankburov.kotlinxsltdsl.unit

import io.ankburov.kotlinxsltdsl.model.Tree
import org.junit.Assert.assertEquals
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
}