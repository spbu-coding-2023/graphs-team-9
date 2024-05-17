package graph

abstract class AdjacencyList(initiallyVertexCount: Int = 0) {
    init{
        for (vertex in 0 until initiallyVertexCount){
            addVertex()
        }
    }
    protected val adjacencyList = ArrayList<ArrayList<Edge>>()

    fun addEdge(
        source: Int,
        target: Int,
        label: String,
        weight: Int,
    ) {
        require(source < verticesCount()) {
            "Adjacency list doesn't have vertex with index $source"
        }
        require(source < verticesCount()) {
            "Adjacency list doesn't have vertex with index $target"
        }
        require(!isEdgeContained(source, target)) {
            "Duplicated edges are not allowed"
        }

        addEdgeToTheAdjacencyList(source, target, label, weight)
    }

    protected abstract fun addEdgeToTheAdjacencyList(
        source: Int,
        target: Int,
        label: String,
        weight: Int,
    )

    fun addVertex(): Int { // Возвращается индекс добавленной вершины (удалите коммент после наприсания доки)
        adjacencyList.add(arrayListOf())
        return adjacencyList.size - 1
    }

    fun outgoingEdgesCount(source: Int): Int {
        require(source < adjacencyList.size) {
            "Adjacency list doesn't have vertex with index $source"
        }
        return adjacencyList[source].size
    }

    fun getEdge(source: Int, edgeOrdinalNumber: Int): Edge {
        val outgoingEdgesCount = outgoingEdgesCount(source)
        require(edgeOrdinalNumber < outgoingEdgesCount) {
            "$source has only $outgoingEdgesCount edges"
        }
        return adjacencyList[source][edgeOrdinalNumber]
    }

    fun verticesCount(): Int {
        return adjacencyList.size
    }

    private fun isEdgeContained(source: Int, target: Int): Boolean {
        for (edge in adjacencyList[source]) {
            if (edge.target() == target) {
                return true
            }
        }
        return false
    }

}
