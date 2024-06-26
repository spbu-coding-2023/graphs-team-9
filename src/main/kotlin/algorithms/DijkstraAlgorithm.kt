package algorithms

import graph.AdjacencyList
import java.util.PriorityQueue

class DijkstraAlgorithm(private val adjacencyList: AdjacencyList) {
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
        val maxDouble = Double.MAX_VALUE
        val comparator =
            Comparator<Pair<Int, Double>> { a, b ->
                a.second.compareTo(b.second)
            }
        val queue = PriorityQueue(comparator)
        val parents = IntArray(verticesCount)

        val distances = HashMap<Int, Double>(verticesCount)
        for (toVertexNumber in 0 until verticesCount) {
            distances[toVertexNumber] = maxDouble
        }
        distances[startVertexIndex] = 0.0
        parents[startVertexIndex] = -1

        queue.add(startVertexIndex to 0.0)
        while (queue.isNotEmpty()) {
            val currentVertexIndex = queue.remove().first
            for (ordinalNumber in 0 until adjacencyList.outgoingEdgesCount(currentVertexIndex)) {
                val neighbourEdge = adjacencyList.getEdge(currentVertexIndex, ordinalNumber)
                val neighbourVertexIndex = neighbourEdge.target()
                val weightOfNeighbourEdge: Double = neighbourEdge.weight()

                val toNeighbourVertexDistance = distances.getValue(neighbourVertexIndex)
                val potentialWeight: Double = distances.getValue(currentVertexIndex) + weightOfNeighbourEdge
                if (toNeighbourVertexDistance > potentialWeight) {
                    distances[neighbourVertexIndex] = potentialWeight
                    queue.add(neighbourVertexIndex to toNeighbourVertexDistance)
                    parents[neighbourVertexIndex] = currentVertexIndex
                }
            }
        }

        require(distances[endVertexIndex] != maxDouble) {
            println("Vertex $endVertexIndex is unreachable from vertex $startVertexIndex")
        }
        return extractPathArray(endVertexIndex, parents)
    }
}
