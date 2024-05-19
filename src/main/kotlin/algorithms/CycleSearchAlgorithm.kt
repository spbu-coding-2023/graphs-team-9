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
        if (vertexIndicesForTheCycleArray.indexOf(vertexIndex) == -1) {
            println("There are no cycles for a given vertex in the graph")
            return
        }
        vertexIndicesForTheCycleArray.reverse()

        val sb = StringBuilder()
        vertexIndicesForTheCycleArray.forEach { sb.append("$it ") }
        println(sb)
    }

    private fun getListOfVerticesForTheCycle(vertexIndex: Int): Pair<Array<Int>?, Int> {
        val stack = Stack<Int>()
        val vertexStatusArray = Array(vertexCount) { VertexStatus.NOT_VISITED }
        val precedingVertexIndicesArray = Array(vertexCount) { -1 } // initialize array with -1
        var prevVertexIndex: Int = -1

        stack.push(vertexIndex)
        while (stack.isNotEmpty()) {
            val currentVertexIndex = stack.pop()
//            if (vertexStatusArray[currentVertexIndex] == VertexStatus.VISITED) {
//                continue
//            }

            for (neighbourEdge in adjacencyList[currentVertexIndex]) {
                if (neighbourEdge.destinationVertexIndex == prevVertexIndex) {
                    continue
                }
                precedingVertexIndicesArray[neighbourEdge.destinationVertexIndex] = currentVertexIndex
                if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.NOT_VISITED) {
                    precedingVertexIndicesArray[neighbourEdge.destinationVertexIndex] = currentVertexIndex
                    stack.push(neighbourEdge.destinationVertexIndex)
                }
                else if (vertexStatusArray[neighbourEdge.destinationVertexIndex] == VertexStatus.VISITED) {
                    val cycleEndVertexIndex = neighbourEdge.destinationVertexIndex
                    return Pair(precedingVertexIndicesArray, cycleEndVertexIndex)
                }
            }
            prevVertexIndex = currentVertexIndex
            vertexStatusArray[currentVertexIndex] = VertexStatus.VISITED
        }
        return Pair(null, -1)
    }
}
