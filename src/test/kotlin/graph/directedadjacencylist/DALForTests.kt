package graph.directedadjacencylist

import graph.DirectedAdjacencyList

class DALForTests(initiallyVertexCount: Int = 0) : DirectedAdjacencyList(initiallyVertexCount) {
    fun adjacencyList() = adjacencyList

//    public override fun addEdgeToTheAdjacencyList(
//        source: Int,
//        target: Int,
//        label: String,
//        weight: Double,
//    ) {
//        adjacencyList[source].add(Edge(target, label, weight))
//    }
}