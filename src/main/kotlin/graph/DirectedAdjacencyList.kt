package graph

class DirectedAdjacencyList(initiallyVertexCount: Int = 0) : AdjacencyList(initiallyVertexCount) {
    override fun addEdgeToTheAdjacencyList(
        sourceVertexIndex: Int,
        destinationVertexIndex: Int,
        label: String,
        weight: Int,
    ) {
        adjacencyList[sourceVertexIndex].add(Edge(destinationVertexIndex, label, weight))
    }
}
