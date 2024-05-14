package algorithms

import graph.Graph
import kotlin.Int.Companion.MAX_VALUE

class BellmanFordAlgorithm<V>(graph: Graph<V>) {
    private val verticesCount = graph.getVerticesCount()
    private val adjacencyList = graph.getTheAdjacencyList()
    private val pathLengthTable = IntArray(verticesCount) { MAX_VALUE }
    private val parentsTable = IntArray(verticesCount) { -1 }

    private fun buildTables(start: Int) {
        pathLengthTable[start] = 0
        for (iteration in 0 until verticesCount) {
            var isTableChanges: Boolean = false
            for (vertex in 0 until verticesCount) {
                val id = (start + vertex) % verticesCount
                if (pathLengthTable[id] != MAX_VALUE) {
                    for (edge in adjacencyList[id]) {
                        val destination = edge.destinationVertexIndex
                        if (pathLengthTable[destination] > pathLengthTable[id] + edge.weight.toInt()) {
                            pathLengthTable[destination] = pathLengthTable[id] + edge.weight.toInt()
                            parentsTable[destination] = id
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
