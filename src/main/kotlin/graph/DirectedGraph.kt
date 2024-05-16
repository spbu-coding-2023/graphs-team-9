package graph

import algorithms.BellmanFordAlgorithm
import algorithms.TarjanSAlgo

class DirectedGraph<V> : Graph<V>() {
    override val adjacencyList = DirectedAdjacencyList()

    override fun getTheAdjacencyList(): DirectedAdjacencyList {
        return adjacencyList
    }

    override fun getShortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>? {
        val algo = BellmanFordAlgorithm(this)
        val vertexValues = getVertexValues()
        var idStart = -1
        var idEnd = -1
        when (isAbleToAdd) {
            true -> {
                if (vertexIndicesMap[start] == null || vertexIndicesMap[end] == null) {
                    throw IllegalArgumentException(
                        "Vertices can not be null",
                    )
                }
                idStart = vertexIndicesMap.getValue(start)
                idEnd = vertexIndicesMap.getValue(end)
            }
            false -> {
                for (i in 0 until getVerticesCount()) {
                    if (vertexValues[i] == start) idStart = i
                    if (vertexValues[i] == end) idEnd = i
                }
                if (idStart == -1 || idEnd == -1) throw IllegalArgumentException("Vertices can not be null")
            }
        }
        return algo.findPath(idStart, idEnd)
    }

    override fun getStronglyComponents(): ArrayList<ArrayList<Int>> {
        val tarjanSAlgo = TarjanSAlgo(this)
        return tarjanSAlgo.tarjanSAlgo()
    }
}
