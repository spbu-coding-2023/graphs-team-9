package algorithms

import graph.Graph
import java.util.Stack

class CycleSearchAlgorithm<V>(graph: Graph<V>) {
    private val vertexCount = graph.getVerticesCount()
    private val adjacencyList = graph.getTheAdjacencyList()

    enum class VertexStatus {
        NOT_VISITED,
        IN_PROCESSING,
        PROCESSED,
    }

    fun outputTheCycleForVertex(vertexIndex: Int) {
        val (precedingVertexIndicesArray, cycleEndVertexIndex) =
            getListOfVerticesForTheCycle(
                vertexIndex,
            )
        if (precedingVertexIndicesArray == null) {
            println("There are no cycles for a given vertex in the graph")
            return
        }

        val vertexIndicesForTheCycleArray: MutableList<Int> = mutableListOf()
        vertexIndicesForTheCycleArray.add(cycleEndVertexIndex)
        var currVertexIndex = cycleEndVertexIndex
        do {
            currVertexIndex = precedingVertexIndicesArray[currVertexIndex]
            vertexIndicesForTheCycleArray.add(currVertexIndex)
        } while (cycleEndVertexIndex != precedingVertexIndicesArray[currVertexIndex])
        vertexIndicesForTheCycleArray.reverse()

        val sb = StringBuilder()
        vertexIndicesForTheCycleArray.forEach { sb.append(it) }
        println(sb)
    }

    private fun getListOfVerticesForTheCycle(vertexIndex: Int): Pair<Array<Int>?, Int> {
        val stack = Stack<Int>()
        val vertexStatusArray = Array(vertexCount) { VertexStatus.NOT_VISITED }
        val precedingVertexIndicesArray = Array(vertexCount) { -1 } // initialize array with -1

        stack.push(vertexIndex)
        while (stack.isNotEmpty()) {
            val currentVertexIndex = stack.pop()
            vertexStatusArray[currentVertexIndex] = VertexStatus.IN_PROCESSING
            for (neighbourEdge in adjacencyList[currentVertexIndex - 1]) {
                precedingVertexIndicesArray[neighbourEdge.destinationVertexIndex] = currentVertexIndex
                if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.IN_PROCESSING) {
                    val cycleEndVertexIndex = neighbourEdge.destinationVertexIndex
                    return Pair(precedingVertexIndicesArray, cycleEndVertexIndex)
                } else if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.NOT_VISITED) {
                    stack.push(neighbourEdge.destinationVertexIndex)
                }
            }
            vertexStatusArray[currentVertexIndex] = VertexStatus.PROCESSED
        }
        return Pair(null, -1)
    }
}
