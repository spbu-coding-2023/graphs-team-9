package graph

import algorithms.FindBridgesAlgorithm

class UndirectedGraph<V> : Graph<V>() {
    override fun addEdgeToAdjacencyList(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Int,
    ) {
        adjacencyList[firstVertexInd].add(Edge(secondVertexInd, label, weight))
        adjacencyList[secondVertexInd].add(Edge(firstVertexInd, label, weight))
    }

    override fun findBridges(): MutableList<IntArray> {
        val algo = FindBridgesAlgorithm(this)
        return algo.findBridges()
    }

    override fun getStronglyComponents(): ArrayList<ArrayList<Int>> {
        throw UnsupportedOperationException("getStronglyComponent() hasn't implemented for undirected graphs")
    }
}
