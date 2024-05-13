package algorithms

import graph.UndirectedGraph
import kotlin.math.min

class FindBridgesAlgorithm<V>(graph: UndirectedGraph<V>) {
    private val adjacencyList = graph.getTheAdjacencyList()
    private val verticesCount = graph.getVerticesCount()
    private val visited = BooleanArray(verticesCount) { false }
    private val tin = IntArray(verticesCount) { -1 }
    private val fup = IntArray(verticesCount) { -1 }
    private var timer: Int = 0
    private val bridges: MutableList<IntArray> = mutableListOf()

    private fun dfs(
        vertex: Int,
        parent: Int = -1,
    ) {
        visited[vertex] = true
        tin[vertex] = timer
        fup[vertex] = timer++
        for (edge in adjacencyList[vertex]) {
            if (parent == edge.destinationVertexIndex) continue
            if (visited[edge.destinationVertexIndex]) {
                fup[vertex] = min(fup[vertex], tin[edge.destinationVertexIndex])
            } else {
                dfs(edge.destinationVertexIndex, vertex)
                fup[vertex] = min(fup[vertex], fup[edge.destinationVertexIndex])
                if (fup[edge.destinationVertexIndex] > tin[vertex]) {
                    bridges.add(intArrayOf(vertex, edge.destinationVertexIndex))
                }
            }
        }
    }

    fun findBridges(): MutableList<IntArray> {
        for (vertex in 0 until verticesCount) if (!visited[vertex]) dfs(vertex)
        return bridges
    }
}
