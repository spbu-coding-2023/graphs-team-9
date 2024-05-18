package graph

class UndirectedAdjacencyList : AdjacencyList() {
    override fun addEdgeToTheAdjacencyList(
        sourceVertexIndex: Int,
        destinationVertexIndex: Int,
        label: String,
        weight: Int,
    ) {
        adjacencyList[sourceVertexIndex].add(Edge(destinationVertexIndex, label, weight))
        adjacencyList[destinationVertexIndex].add(Edge(sourceVertexIndex, label, weight))
    }
}
