package algorithms

import Graph
import java.util.PriorityQueue

class DijkstraShortestPathAlgorithm<V>(graph: Graph<V>) {
    private val verticesCount = graph.getVerticesCount()
    private val adjacencyList = graph.getTheAdjacencyList()

    private fun getArrayWithNeighbourVertex(vertex: Pair<Int, Int>): Array<Pair<Int, Int>> {
		TODO()
    }

    private fun getEdgeWeight(
        fromVertex: Pair<Int, Int>,
        toVertex: Pair<Int, Int>,
    ): Int {
        TODO()
    }

    fun findShortestPathDijkstra(
        sourceVertexNumber: Int,
        destinationVertexNumber: Int,
    ) {
        // distances map initializing with big values
        val distances = hashMapOf<Int, Int>()
        for (toVertexNumber in 1..verticesCount) {
            distances[toVertexNumber] = Int.MAX_VALUE
        }

        // visited boolean array initializing
        val visited = BooleanArray(verticesCount)

        // PriorityQueue initializing
        // comparator is needed so that the pairs whose second value is the smallest are at the beginning of the queue
        val comparator =
            Comparator<Pair<Int, Int>> { a, b ->
                a.second.compareTo(b.second)
            }
        val queue = PriorityQueue(comparator)

        queue.add(Pair(sourceVertexNumber, 0))
        distances[sourceVertexNumber] = 0

        while (queue.isNotEmpty()) {
            val currentVertex: Pair<Int, Int> = queue.remove()
            if (!visited[currentVertex.first]) continue
            visited[currentVertex.first] = true

            for (neighbourVertex in getArrayWithNeighbourVertex(currentVertex)) {
                // туть подумать, как обойтись без каста и '!!'
                val potentialDistance: Int =
                    currentVertex.let {
                        distances[it.first] as Int +
                            getEdgeWeight(
                                currentVertex,
                                neighbourVertex,
                            )
                    }
                if (distances[neighbourVertex.first]?.let { potentialDistance.compareTo(it) }!! < 0) {
                    distances[neighbourVertex.first] = potentialDistance
                }
                queue.add(Pair(neighbourVertex.first, distances[neighbourVertex.first]) as Pair<Int, Int>)
            }
        }
        println(distances[destinationVertexNumber])
    }
}
