package graph.directedadjacencylist

import graph.DirectedAdjacencyList

class DALForTests(initiallyVertexCount: Int = 0) : DirectedAdjacencyList(initiallyVertexCount) {
    fun adjacencyList() = adjacencyList
}