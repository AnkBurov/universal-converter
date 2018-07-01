package io.ankburov.kotlinxsltdsl.model

/**
 * A nice tree
 *
 * Not a binary one - every node can have many children
 * Each node knows its position in the tree hierarchy by using its keysHierarchy
 *
 * Example:
 * firstLevelNode/secondLevelNode/thirdLevelNode
 * It means that thirdLevelNode will be a child of secondLevelNode which is a child of firstLevelNode which is a child of root node
 *
 * Each node knows whether or not their children contain searching node thus resulting the search algorithm is more efficient than DFS and BFS
 *
 * Not generized due to the need of persisting different types and be able to create intermediate nodes when inserting without messing with nulls safety
 */
class Tree(internal val root: Node<Any>) {

    fun addChild(key: String, value: Any) {
        val keyHierarchy = key.trim().split("/")

        addChildRecursive(0, keyHierarchy, value, root)
    }

    fun getValue(key: String): Any? {
        val keyHierarchy = key.trim().split("/")

        return getChildRecursive(0, keyHierarchy, root)?.value
    }

    private fun getChildRecursive(depth: Int, keysHierarchy: List<String>, upperNode: Node<Any>): Node<Any>? {
        if (depth == keysHierarchy.size) {
            return upperNode
        }

        return upperNode.children
                .mapNotNull {
                    if (it.keyHierarchy[depth] == keysHierarchy[depth]) {
                        getChildRecursive(depth + 1, keysHierarchy, it)
                    }
                    else null
                }
                .firstOrNull()
    }

    private fun addChildRecursive(depth: Int, keysHierarchy: List<String>, value: Any, upperNode: Node<Any>) {
        if (depth == keysHierarchy.size - 1) {
            val newNode = Node(keysHierarchy, value, upperNode)
            val iterator = upperNode.children.iterator()
            while (iterator.hasNext()) {
                val child = iterator.next()
                if (child.keyHierarchy[depth] == keysHierarchy[depth]) {
                    iterator.remove()
                }
            }
            upperNode.children.add(newNode)
        } else {
            val filteredNodes = upperNode.children
                    .filter { it.keyHierarchy[depth] == keysHierarchy[depth] }

            if (filteredNodes.isEmpty()) {
                val newNode = Node(keysHierarchy.subList(0, depth + 1), value, upperNode)
                upperNode.children.add(newNode)
                addChildRecursive(depth + 1, keysHierarchy, value, newNode)
            } else {
                addChildRecursive(depth + 1, keysHierarchy, value, filteredNodes.first())
            }
        }
    }

    class Node<T>(val keyHierarchy: List<String>,
                  val value: T,
                  val parent: Node<T>? = null,
                  val children: MutableList<Node<T>> = arrayListOf()) {

        operator fun String.invoke(value: T, init: (Tree.Node<T>.() -> Unit)? = null) {
            node(this, value, init)
        }
    }

    companion object {
        fun new() = Tree(Tree.Node(listOf(ROOT_KEY), ""))

        const val ROOT_KEY = "root"
    }
}
