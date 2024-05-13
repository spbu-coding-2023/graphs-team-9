package graph

import algorithms.BellmanFordAlgorithm
import algorithms.TarjanSAlgo

class DirectedGraph<V> : Graph<V>() {
    override fun addEdgeToAdjacencyList(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Int,
    ) {
        adjacencyList[firstVertexInd].add(Edge(secondVertexInd, label, weight))
    }

    override fun getShortestPath(
        start: V,
        end: V,
    ): MutableList<Int>? {
        val algo = BellmanFordAlgorithm(this)
        val vertexValues = getVertexValues()
        var idStart = -1
        var idEnd = -1
        for (i in 0 until getVerticesCount()){
            if (vertexValues[i] == start) idStart = i
            if (vertexValues[i] == end) idEnd = i
        }
        if (idStart == -1 || idEnd == -1) throw IllegalArgumentException("Vertices can not be null")
        return algo.findPath(idStart, idEnd)
    }

    override fun getStronglyComponents(): ArrayList<ArrayList<Int>> {
        val tarjanSAlgo = TarjanSAlgo(this)
        return tarjanSAlgo.tarjanSAlgo()
    }
}
