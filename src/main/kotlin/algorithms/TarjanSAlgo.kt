package algorithms

import graph.DirectedAdjacencyList
import java.util.Stack

class TarjanSAlgo(private val adjacencyList: DirectedAdjacencyList) {
    private val stronglyConnectedComponents: ArrayList<ArrayList<Int>> = arrayListOf()
    private var order = 0
    private val verticesStack = Stack<Int>()
    private val verticesCount = adjacencyList.verticesCount()
    private val orders = IntArray(verticesCount) { -1 }
    private val prevs = IntArray(verticesCount) { -1 }
    private val edgesNotCheckedCounts = IntArray(verticesCount) { v -> adjacencyList.outgoingEdgesCount(v) }
    private val leastIndLinks = IntArray(verticesCount) { -1 }
    private val verticesStackAffiliations = BooleanArray(verticesCount) { false }

    fun tarjanSAlgo(): ArrayList<ArrayList<Int>> {
        for (vertex in 0 until verticesCount) {
            if (orders[vertex] == -1) {
                dfs(vertex)
            }
        }
        return stronglyConnectedComponents
    }

    private fun dfs(startVertex: Int) {
        val dfsStack = Stack<Int>()
        dfsStack.add(startVertex)
        while (dfsStack.isNotEmpty()) {
            val curVertex = dfsStack.pop()
            if (orders[curVertex] == -1) {
                orders[curVertex] = order
                leastIndLinks[curVertex] = order++
                verticesStack.add(curVertex)
                verticesStackAffiliations[curVertex] = true
            }
            var shouldCheckAdjacentVertex = false
            while (edgesNotCheckedCounts[curVertex] != 0 && !shouldCheckAdjacentVertex) {
                val edgeOrdinalNumber = --edgesNotCheckedCounts[curVertex]
                val adjacentVertex = adjacencyList.getEdge(curVertex, edgeOrdinalNumber).target()
                if (orders[adjacentVertex] == -1) {
                    prevs[adjacentVertex] = curVertex
                    dfsStack.push(adjacentVertex)
                    shouldCheckAdjacentVertex = true
                } else {
                    if (verticesStackAffiliations[adjacentVertex]) {
                        leastIndLinks[curVertex] = minOf(leastIndLinks[curVertex], orders[adjacentVertex])
                    }
                }
            }
            if (shouldCheckAdjacentVertex) {
                break
            }
            if (edgesNotCheckedCounts[curVertex] == 0) {
                if (orders[curVertex] == leastIndLinks[curVertex]) {
                    createStronglyConnectedComponent(curVertex)
                }
                val prevVertex = prevs[curVertex]
                if (prevVertex != -1) {
                    leastIndLinks[prevVertex] = minOf(leastIndLinks[prevVertex], leastIndLinks[curVertex])
                    dfsStack.push(prevVertex)
                }
            }
        }
    }

    private fun createStronglyConnectedComponent(formativeVertex: Int) {
        var curVertex: Int
        val stronglyConnectedComponent = ArrayList<Int>()
        do {
            curVertex = verticesStack.pop()
            verticesStackAffiliations[curVertex] = false
            stronglyConnectedComponent.add(curVertex)
        } while (curVertex != formativeVertex)
        stronglyConnectedComponents.add(stronglyConnectedComponent)
    }
}
