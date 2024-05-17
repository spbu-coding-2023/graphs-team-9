package graph

open class UndirectedAdjacencyList(initiallyVertexCount: Int = 0) : AdjacencyList(initiallyVertexCount) {
    override fun addEdgeToTheAdjacencyList(
        source: Int,
        target: Int,
        label: String,
        weight: Number
    ) {
        adjacencyList[source].add(Edge(target, label, weight))
        adjacencyList[target].add(Edge(source, label, weight))
    }
}
