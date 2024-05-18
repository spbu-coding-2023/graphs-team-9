package graph

import algorithms.BellmanFordAlgorithm

class UndirectedGraph<V>(
    override val adjacencyList: UndirectedAdjacencyList = UndirectedAdjacencyList(),
    override val vertexValues: ArrayList<V> = arrayListOf(),
) : Graph<V>() {

    init {
        require(adjacencyList.getVerticesCount() == vertexValues.size) {
            "vertexValues size isn't equal adjacencyList's vertices count"
        }
    }

    override fun getTheAdjacencyList(): UndirectedAdjacencyList {
        return adjacencyList
    }

    override fun getShortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>? {
        if (hasNegativeWeights) throw UnsupportedOperationException("getStronglyComponent() hasn't implemented for undirected graphs")

        val algo = BellmanFordAlgorithm(adjacencyList)
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
                    if (getVertexValue(i) == start) idStart = i
                    if (getVertexValue(i) == end) idEnd = i
                }
                if (idStart == -1 || idEnd == -1) throw IllegalArgumentException("Vertices can not be null")
            }
        }
        return algo.findPath(idStart, idEnd)
    }

    override fun getStronglyConnectedComponents(): ArrayList<ArrayList<Int>> {
        throw UnsupportedOperationException("getStronglyComponent() hasn't implemented for undirected graphs")
    }
}
