package algorithms

import graph.AdjacencyList
import kotlin.Double.Companion.MAX_VALUE

class BellmanFordAlgorithm(private val adjacencyList: AdjacencyList) {
    private val verticesCount = adjacencyList.verticesCount()
    private val pathLengthTable = DoubleArray(verticesCount) { MAX_VALUE }
    private val parentsTable = IntArray(verticesCount) { -1 }

    private fun buildTables(start: Int) {
        pathLengthTable[start] = 0.0
        for (iteration in 0 until verticesCount) {
            var isTableChanges = false
            for (vertex in 0 until verticesCount) {
                val source = (start + vertex) % verticesCount
                if (pathLengthTable[source] != MAX_VALUE) {
                    for (idEdge in 0 until adjacencyList.outgoingEdgesCount(source)) {
                        val edge = adjacencyList.getEdge(source, idEdge)
                        val target = edge.target()
                        if (pathLengthTable[target] > pathLengthTable[source] + edge.weight()) {
                            pathLengthTable[target] = pathLengthTable[source] + edge.weight()
                            parentsTable[target] = source
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
