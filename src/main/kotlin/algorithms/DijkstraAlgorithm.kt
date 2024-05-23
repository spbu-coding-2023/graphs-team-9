package algorithms

import graph.AdjacencyList
import java.util.PriorityQueue

class DijkstraAlgorithm<V>(private val adjacencyList: AdjacencyList) {
    private val verticesCount = adjacencyList.verticesCount()

    private fun extractPathArray(
        endVertexIndex: Int,
        parentsArray: IntArray,
    ): ArrayList<Int> {
        val pathArray = ArrayList<Int>()
        var currVertexIndex = endVertexIndex
        do {
            pathArray.add(currVertexIndex)
            currVertexIndex = parentsArray[currVertexIndex]
        } while (currVertexIndex != -1)
        pathArray.reverse()
        return pathArray
    }

    fun findShortestPath(
        startVertexIndex: Int,
        endVertexIndex: Int,
    ): ArrayList<Int> {
        val comparator =
            Comparator<Pair<Int, Number>> { a, b ->
                a.second.toInt().compareTo(b.second.toInt())
            }
        val queue = PriorityQueue(comparator)
        val parents = IntArray(verticesCount)
        parents[startVertexIndex] = -1

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
                    parents[neighbourVertexIndex] = currentVertexIndex
                }
            }
        }

        require(distances[endVertexIndex] != maxInt) {
            println("Vertex $endVertexIndex is unreachable from vertex $startVertexIndex")
        }
        return extractPathArray(endVertexIndex, parents)
    }
}
