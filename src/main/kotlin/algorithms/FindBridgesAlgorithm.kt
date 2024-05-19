package algorithms

import graph.UndirectedAdjacencyList
import java.util.Stack
import kotlin.math.min

class FindBridgesAlgorithm(private val adjacencyList: UndirectedAdjacencyList) {
    private val verticesCount = adjacencyList.verticesCount()
    private val dfsStack = Stack<Int>()
    private val idStartVertex = IntArray(verticesCount) { 0 }
    private val visited = BooleanArray(verticesCount) { false }
    private val enterTimeInVertex = IntArray(verticesCount) { -1 }
    private val enterTimeInConnectedComponent = IntArray(verticesCount) { -1 }
    private var timer: Int = 0
    private val bridges: MutableSet<Set<Int>> = mutableSetOf()

    private fun dfs(
        startVertex: Int,
        startParent: Int = -1,
    ) {
        var parent = startParent
        var vertex = startVertex
        dfsStack.push(vertex)
        visited[vertex] = true
        enterTimeInVertex[vertex] = timer
        enterTimeInConnectedComponent[vertex] = timer++

        while (!dfsStack.isEmpty()) {
            for (idEdge in idStartVertex[vertex] until adjacencyList.outgoingEdgesCount(vertex)) {
                ++idStartVertex[vertex]
                val edge = adjacencyList.getEdge(vertex, idEdge)
                if (parent == edge.target()) continue
                if (visited[edge.target()]) {
                    enterTimeInConnectedComponent[vertex] =
                        min(
                            enterTimeInConnectedComponent[vertex],
                            enterTimeInVertex[edge.target()],
                        )
                } else {
                    dfsStack.push(edge.target())
                    parent = vertex
                    vertex = edge.target()
                    break
                }
            }

            if (visited[vertex]) {
                when (dfsStack.size) {
                    1 -> dfsStack.pop()
                    else -> {
                        val destination = dfsStack.pop()
                        val source = dfsStack.pop()
                        enterTimeInConnectedComponent[source] =
                            min(
                                enterTimeInConnectedComponent[source],
                                enterTimeInConnectedComponent[destination],
                            )
                        if (enterTimeInConnectedComponent[destination] > enterTimeInVertex[source]) {
                            bridges.add(setOf(source, destination))
                        }
                        vertex = source
                        parent =
                            when (dfsStack.isEmpty()) {
                                true -> -1
                                else -> dfsStack.peek()
                            }
                        dfsStack.push(vertex)
                    }
                }
            } else {
                visited[vertex] = true
                enterTimeInVertex[vertex] = timer
                enterTimeInConnectedComponent[vertex] = timer++
            }
        }
    }

    fun findBridges(): MutableSet<Set<Int>> {
        for (vertex in 0 until verticesCount) if (!visited[vertex]) dfs(vertex)
        return bridges
    }
}
