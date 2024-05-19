package algorithms

import graph.UndirectedGraph
import java.util.Stack

class CycleSearchAlgorithm<V>(graph: UndirectedGraph<V>) {
    private val vertexCount = graph.getVerticesCount()
    private val adjacencyList = graph.getTheAdjacencyList()

    enum class VertexStatus {
        NOT_VISITED,
        VISITED,
    }

    fun printCycleForVertex(vertexIndex: Int) {
        val (parentVertexIndicesArray, cycleEndVertexIndex) =
            findCycleForVertex(vertexIndex)
        val vertexIndicesForCycleArray: MutableList<Int> = mutableListOf()
        var currVertexIndex = cycleEndVertexIndex

        if (parentVertexIndicesArray == null) {
            println("There are no cycles for a given vertex in the graph")
            return
        }
        vertexIndicesForCycleArray.add(cycleEndVertexIndex)
        do {
            currVertexIndex = parentVertexIndicesArray[currVertexIndex]
            vertexIndicesForCycleArray.add(currVertexIndex)
        } while (cycleEndVertexIndex != parentVertexIndicesArray[currVertexIndex])
        if (vertexIndicesForCycleArray.indexOf(vertexIndex) == -1) {
            println("There are no cycles for a given vertex in the graph")
            return
        }
        vertexIndicesForCycleArray.reverse()

        val sb = StringBuilder()
        vertexIndicesForCycleArray.forEach { sb.append("$it ") }
        println(sb)
    }

    private fun findCycleForVertex(vertexIndex: Int): Pair<Array<Int>?, Int> {
        val stack = Stack<Int>()
        val visitedOrNotArray = Array(vertexCount) { VertexStatus.NOT_VISITED }
        val parentVertexIndicesArray = Array(vertexCount) { -1 } // initialize array with -1
        var prevVertexIndex: Int = -1

        stack.push(vertexIndex)
        while (stack.isNotEmpty()) {
            val currentVertexIndex = stack.pop()
            for (neighbourEdge in adjacencyList[currentVertexIndex]) {
                val neighbourVertexIndex = neighbourEdge.destinationVertexIndex
                if (neighbourVertexIndex == prevVertexIndex) {
                    continue
                }
                parentVertexIndicesArray[neighbourVertexIndex] = currentVertexIndex
                if (visitedOrNotArray[neighbourVertexIndex] == VertexStatus.NOT_VISITED) {
                    parentVertexIndicesArray[neighbourVertexIndex] = currentVertexIndex
                    stack.push(neighbourVertexIndex)
                } else if (visitedOrNotArray[neighbourVertexIndex] == VertexStatus.VISITED) {
                    return parentVertexIndicesArray to neighbourVertexIndex   // neighbourVertexIndex is the end of cycle
                }
            }
            prevVertexIndex = currentVertexIndex
            visitedOrNotArray[currentVertexIndex] = VertexStatus.VISITED
        }
        return null to -1
    }
}
