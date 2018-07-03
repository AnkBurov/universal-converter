package io.ankburov.universalconverter.model

import io.ankburov.universalconverter.model.Tree.Companion.ROOT_KEY
import io.ankburov.universalconverter.utils.startsWith

object treeDsl {
    operator fun invoke(init: Tree.Node<Any>.() -> Unit): Tree {
        val newTree = Tree.new()
        val root = newTree.root
        root.init()
        return newTree;
    }
}

fun <T> Tree.Node<T>.node(key: String, value: T? = null, init: (Tree.Node<T>.() -> Unit)? = null) {
    val newNodeKeysHierarchy = when {
        keyHierarchy.startsWith { it == ROOT_KEY } -> ArrayList<String>(keyHierarchy.subList(1, keyHierarchy.size))
        else -> ArrayList<String>(keyHierarchy)
    }
    newNodeKeysHierarchy += key
    val newNode = Tree.Node<T>(newNodeKeysHierarchy, value, this)
    children.add(newNode)
    if (init != null) {
        newNode.init()
    }
}