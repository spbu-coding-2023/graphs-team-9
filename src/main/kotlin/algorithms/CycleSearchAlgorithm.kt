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

    fun outputTheCycleForVertex(vertexNumber: Int) {
        val (precedingVertexNumberArray, cycleEndVertexNumber) = getListOfVerticesForACycleIfTheGraphIsCyclic(vertexNumber)
        if (precedingVertexNumberArray == null) {
            println("There are no cycles for a given vertex in the graph")
            return
        }

        val verticesForTheCycleArray: MutableList<Int> = mutableListOf()
        verticesForTheCycleArray.add(cycleEndVertexNumber)
        var currVertexIndex = cycleEndVertexNumber
        do {
            currVertexIndex = precedingVertexNumberArray[currVertexIndex]
            verticesForTheCycleArray.add(currVertexIndex)
        } while (cycleEndVertexNumber != precedingVertexNumberArray[currVertexIndex])
        verticesForTheCycleArray.reverse()

        val sb = StringBuilder()
        verticesForTheCycleArray.forEach { sb.append(it) }
        println(sb)
    }

    private fun getListOfVerticesForACycleIfTheGraphIsCyclic(vertexNumber: Int): Pair<Array<Int>?, Int> {
        val stack = Stack<Int>()
        val vertexStatusArray = Array(vertexCount) { VertexStatus.NOT_VISITED }
        val precedingVerticesArray = Array(vertexCount) { -1 } // initialize array with -1

        stack.push(vertexNumber)
        while (stack.isNotEmpty()) {
            val currentVertexNumber = stack.pop()
            vertexStatusArray[currentVertexNumber] = VertexStatus.IN_PROCESSING
            for (neighbourEdge in adjacencyList[currentVertexNumber - 1]) {
                precedingVerticesArray[neighbourEdge.destinationVertexIndex] = currentVertexNumber
                if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.IN_PROCESSING) {
                    val cycleEndVertexNumber = neighbourEdge.destinationVertexIndex
                    return Pair(precedingVerticesArray, cycleEndVertexNumber)
                } else if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.NOT_VISITED) {
                    stack.push(neighbourEdge.destinationVertexIndex)
                }
            }
            vertexStatusArray[currentVertexNumber] = VertexStatus.PROCESSED
        }
        return Pair(null, -1)
    }
}
