package graph

class DirectedAdjacencyList : AdjacencyList() {
    override fun addEdgeToTheAdjacencyList(
        sourceVertexIndex: Int,
        destinationVertexIndex: Int,
        label: String,
        weight: Int,
    ) {
        adjacencyList[sourceVertexIndex].add(Edge(destinationVertexIndex, label, weight))
    }
}
