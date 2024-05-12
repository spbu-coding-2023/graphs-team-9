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

    override fun getShortestPath(
        start: V,
        end: V,
    ): MutableList<V>? {
        throw UnsupportedOperationException("getShortestPath() hasn't implemented for undirected graphs")
    }
}
