package algorithms

import graph.AdjacencyList
import kotlin.Int.Companion.MAX_VALUE

class BellmanFordAlgorithm(private val adjacencyList: AdjacencyList) {
    private val verticesCount = adjacencyList.getVerticesCount()
    private val pathLengthTable = IntArray(verticesCount) { MAX_VALUE }
    private val parentsTable = IntArray(verticesCount) { -1 }

    private fun buildTables(start: Int) {
        pathLengthTable[start] = 0
        for (iteration in 0 until verticesCount) {
            var isTableChanges: Boolean = false
            for (vertex in 0 until verticesCount) {
                val source = (start + vertex) % verticesCount
                if (pathLengthTable[source] != MAX_VALUE) {
                    for (idEdge in 0 until adjacencyList.getOutgoingEdgesCount(source)) {
                        val edge = adjacencyList.getEdge(source, idEdge)
                        val destination = edge.destinationVertexIndex
                        if (pathLengthTable[destination] > pathLengthTable[source] + edge.weight.toInt()) {
                            pathLengthTable[destination] = pathLengthTable[source] + edge.weight.toInt()
                            parentsTable[destination] = source
                            isTableChanges = true
                        }
                    }
                }
            }
            if (!isTableChanges) {
                break
            } else if (iteration == verticesCount - 1) {
                throw UnsupportedOperationException("Graph contains negative weight cycle")
            }
        }
    }

    fun findPath(
        start: Int,
        end: Int,
    ): MutableList<Int>? {
        buildTables(start)
        val path: MutableList<Int> = mutableListOf()
        var currentVertex = end
        path.add(currentVertex)
        if (start == end) return path
        if (parentsTable[end] == -1) return null
        while (parentsTable[currentVertex] != -1) {
            path.add(parentsTable[currentVertex])
            currentVertex = parentsTable[currentVertex]
        }
        path.reverse()
        return path
    }
}
