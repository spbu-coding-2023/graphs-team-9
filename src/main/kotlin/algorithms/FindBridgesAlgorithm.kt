package algorithms

import graph.UndirectedAdjacencyList
import java.util.Stack
import kotlin.math.min

class FindBridgesAlgorithm(private val adjacencyList: UndirectedAdjacencyList) {
    private val verticesCount = adjacencyList.getVerticesCount()
    private val verticesStack = Stack<Int>()
    private val idStartVertex = IntArray(verticesCount) { 0 }
    private val visited = BooleanArray(verticesCount) { false }
    private val enterTimeInVertex = IntArray(verticesCount) { -1 }
    private val enterTimeInConnectedComponent = IntArray(verticesCount) { -1 }
    private var timer: Int = 0
    private val bridges: MutableList<IntArray> = mutableListOf()

    private fun dfs(
        startVertex: Int,
        startParent: Int = -1,
    ) {
        var parent = startParent
        var vertex = startVertex
        verticesStack.push(vertex)
        visited[vertex] = true
        enterTimeInVertex[vertex] = timer
        enterTimeInConnectedComponent[vertex] = timer++

        while (!verticesStack.isEmpty()) {
            for (idEdge in idStartVertex[vertex] until adjacencyList.getOutgoingEdgesCount(vertex)) {
                ++idStartVertex[vertex]
                val edge = adjacencyList.getEdge(vertex, idEdge)
                if (parent == edge.destinationVertexIndex) continue
                if (visited[edge.destinationVertexIndex]) {
                    enterTimeInConnectedComponent[vertex] =
                        min(
                            enterTimeInConnectedComponent[vertex],
                            enterTimeInVertex[edge.destinationVertexIndex],
                        )
                } else {
                    verticesStack.push(edge.destinationVertexIndex)
                    parent = vertex
                    vertex = edge.destinationVertexIndex
                    break
                }
            }

            if (visited[vertex]) {
                val destination = verticesStack.pop()
                val source = verticesStack.pop()
                enterTimeInConnectedComponent[source] =
                    min(
                        enterTimeInConnectedComponent[source],
                        enterTimeInConnectedComponent[destination],
                    )
                if (enterTimeInConnectedComponent[destination] > enterTimeInVertex[source]) {
                    bridges.add(intArrayOf(source, destination))
                }
                vertex = source
                if (!verticesStack.isEmpty()) {
                    parent = verticesStack.peek()
                    verticesStack.push(vertex)
                }
            } else {
                visited[vertex] = true
                enterTimeInVertex[vertex] = timer
                enterTimeInConnectedComponent[vertex] = timer++
            }
        }
    }

    fun findBridges(): MutableList<IntArray> {
        for (vertex in 0 until verticesCount) if (!visited[vertex]) dfs(vertex)
        return bridges
    }
}
