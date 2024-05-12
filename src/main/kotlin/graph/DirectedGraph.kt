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
    ): MutableList<V>? {
        val algo = BellmanFordAlgorithm(this)
        if (vertexIndicesMap[end] == null || vertexIndicesMap[end] == null) throw IllegalArgumentException("Vertices can not be null")
        return algo.findPath(vertexIndicesMap.getValue(start), vertexIndicesMap.getValue(end))

    override fun getStronglyComponents(): ArrayList<ArrayList<Int>> {
        val tarjanSAlgo = TarjanSAlgo(this)
        return tarjanSAlgo.tarjanSAlgo()
    }
}
