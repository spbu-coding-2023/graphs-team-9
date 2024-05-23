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
        val maxInt = Int.MAX_VALUE
        val comparator =
            Comparator<Pair<Int, Int>> { a, b ->
                a.second.compareTo(b.second)
            }
        val queue = PriorityQueue(comparator)
        val parents = IntArray(verticesCount)

        val distances = HashMap<Int, Int>(verticesCount)
        for (toVertexNumber in 0 until verticesCount) {
            distances[toVertexNumber] = maxInt
        }
        distances[startVertexIndex] = 0
        parents[startVertexIndex] = -1

        queue.add(startVertexIndex to 0)
        while (queue.isNotEmpty()) {
            val currentVertexIndex = queue.remove().first
            for (ordinalNumber in 0 until adjacencyList.outgoingEdgesCount(currentVertexIndex)) {
                val neighbourEdge = adjacencyList.getEdge(currentVertexIndex, ordinalNumber)
                val neighbourVertexIndex = neighbourEdge.target()
                val weightOfNeighbourEdge: Number = neighbourEdge.weight()

                val toNeighbourVertexDistance = distances.getValue(neighbourVertexIndex)
                val potentialWeight: Int = distances.getValue(currentVertexIndex) + weightOfNeighbourEdge.toInt()
                if (toNeighbourVertexDistance > potentialWeight) {
                    distances[neighbourVertexIndex] = potentialWeight
                    queue.add(neighbourVertexIndex to toNeighbourVertexDistance)
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
