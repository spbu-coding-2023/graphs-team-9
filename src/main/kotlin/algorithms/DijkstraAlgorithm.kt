package algorithms

import Graph
import java.util.PriorityQueue

class DijkstraAlgorithm<V>(graph: Graph<V>) {
    private val verticesCount = graph.getVerticesCount()
    private val adjacencyList = graph.getTheAdjacencyList()

    fun findShortestPathDijkstra(
        startVertexIndex: Int,
        endVertexIndex: Int,
    ) {
        val comparator =
            Comparator<Pair<Int, Int>> { a, b ->
                a.second.compareTo(b.second)
            }
        val queue = PriorityQueue(comparator)

        val maxInt = Int.MAX_VALUE
        val distances: MutableMap<Int, Int> = mutableMapOf()
        for (toVertexNumber in 1..verticesCount) {
            distances[toVertexNumber] = maxInt
        }
        distances[startVertexIndex] = 0

        queue.add(startVertexIndex to 0)
        while (queue.isNotEmpty()) {
            val currentVertexIndex = queue.remove().first
            for (neighbourEdge in adjacencyList[currentVertexIndex]) {
                val neighbourVertexIndex = neighbourEdge.destinationVertexIndex
                val weightOfNeighbourEdge: Int = neighbourEdge.weight as Int
                // negative weight handler

                // ниже начинается ужас, но пока у меня не получается от него избавиться
                val potentialWeight: Int = distances[currentVertexIndex]?.let { it + weightOfNeighbourEdge } ?: 0
                if (distances[neighbourVertexIndex]!! > potentialWeight) {
                    distances[neighbourVertexIndex] = potentialWeight
                    queue.add((neighbourVertexIndex to distances[neighbourVertexIndex]) as Pair<Int, Int>)
                }
            }
        }
        if (distances[endVertexIndex] == maxInt) {
            println("Vertex $endVertexIndex is unreachable from vertex $startVertexIndex")
            return
        }
        println("Vertex Distance from Source to end is ${distances[endVertexIndex]}")
    }
}
