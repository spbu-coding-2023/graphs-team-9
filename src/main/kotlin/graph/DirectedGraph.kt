package graph

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

    override fun findBridges(): MutableList<IntArray> {
        throw UnsupportedOperationException("findBridges() hasn't implemented for directed graphs")
    }

    override fun getStronglyComponents(): ArrayList<ArrayList<Int>> {
        val tarjanSAlgo = TarjanSAlgo(this)
        return tarjanSAlgo.tarjanSAlgo()
    }
}
