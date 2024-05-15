package graph

abstract class AdjacencyList {
    protected val adjacencyList: ArrayList<ArrayList<Edge>> = arrayListOf()

    fun addEdge(
        sourceVertexIndex: Int,
        destinationVertexIndex: Int,
        label: String,
        weight: Int,
    ) {
        require(sourceVertexIndex < getVerticesCount()) {
            "Adjacency list doesn't have vertex with index $sourceVertexIndex"
        }
        require(destinationVertexIndex < getVerticesCount()) {
            "Adjacency list doesn't have vertex with index $destinationVertexIndex"
        }
        require(isEdgeContained(sourceVertexIndex, destinationVertexIndex)) {
            "Duplicated edges are not allowed"
        }

        addEdgeToTheAdjacencyList(sourceVertexIndex, destinationVertexIndex, label, weight)
    }

    protected abstract fun addEdgeToTheAdjacencyList(
        sourceVertexIndex: Int,
        destinationVertexIndex: Int,
        label: String,
        weight: Int,
    )

    fun addVertex(): Int { // Возвращается индекс добавленной вершины (удалите коммент после наприсания доки)
        adjacencyList.add(arrayListOf())
        return adjacencyList.size - 1
    }

    fun getOutgoingEdgesCount(vertexIndex: Int): Int {
        require(vertexIndex < adjacencyList.size) {
            "Adjacency list doesn't have vertex with index $vertexIndex"
        }
        return adjacencyList[vertexIndex].size
    }

    fun getEdge(sourceVertexIndex: Int, edgeOrdinalNumber: Int): Edge {
        val outgoingEdgesCount = getOutgoingEdgesCount(sourceVertexIndex)
        require(edgeOrdinalNumber < outgoingEdgesCount) {
            "$sourceVertexIndex has only $outgoingEdgesCount edges"
        }
        return adjacencyList[sourceVertexIndex][edgeOrdinalNumber]
    }

    fun getVerticesCount(): Int {
        return adjacencyList.size
    }

    private fun isEdgeContained(sourceVertexIndex: Int, destinationVertexIndex: Int): Boolean {
        for (edge in adjacencyList[sourceVertexIndex]) {
            if (edge.destinationVertexIndex == destinationVertexIndex) {
                return true
            }
        }
        return false
    }
}
