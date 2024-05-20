package algorithms

import graph.Graph
import java.util.PriorityQueue

class DijkstraAlgorithm<V>(graph: Graph<V>) {
    private val verticesCount = graph.verticesCount()
    private val adjacencyList = graph.adjacencyList()
    // handling negative weights

    fun shortestPathByDijkstraAlgorithm(
        startVertexIndex: Int,
        endVertexIndex: Int,
    ) {
        val comparator =
            Comparator<Pair<Int, Number>> { a, b ->
                a.second.toInt().compareTo(b.second.toInt())
            }
        val queue = PriorityQueue(comparator)

        val maxInt = Int.MAX_VALUE
        val distances = HashMap<Int, Number>(verticesCount)
        for (toVertexNumber in 1..verticesCount) {
            distances[toVertexNumber] = maxInt
        }
        distances[startVertexIndex] = 0

        queue.add(startVertexIndex to 0)
        while (queue.isNotEmpty()) {
            val currentVertexIndex = queue.remove().first
            for (ordinalNumber in 0 until adjacencyList.outgoingEdgesCount(currentVertexIndex)) {
                val neighbourEdge = adjacencyList.getEdge(currentVertexIndex, ordinalNumber)
                val neighbourVertexIndex = neighbourEdge.target()
                val weightOfNeighbourEdge: Number = neighbourEdge.weight()

                val potentialWeight: Int = distances[currentVertexIndex] as Int + weightOfNeighbourEdge.toInt()
                if (distances[neighbourVertexIndex] as Int > potentialWeight) {
                    distances[neighbourVertexIndex] = potentialWeight as Number
                    queue.add((neighbourVertexIndex to distances[neighbourVertexIndex]) as Pair<Int, Number>)
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
