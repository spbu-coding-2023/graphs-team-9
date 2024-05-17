package graph

abstract class Graph<V> {
    protected open val vertexValues: ArrayList<V> = arrayListOf()
    protected abstract val adjacencyList: AdjacencyList
    protected var vertexIndicesMap: HashMap<V, Int> = hashMapOf()
    protected var isAbleToAdd = true
    protected var hasNegativeWeights = false
    
    open fun adjacencyList(): AdjacencyList {
        return adjacencyList
    }

    fun vertexValue(vertexIndex: Int): V {
        return vertexValues[vertexIndex]
    }

    fun verticesCount(): Int {
        return adjacencyList.verticesCount()
    }

    fun addVertex(value: V) {
        require(isAbleToAdd) {
            "Not able to add vertices when graph is immutable"
        }
        if (vertexIndicesMap[value] == null) {
            vertexIndicesMap[value] = adjacencyList.addVertex()
        }
        vertexValues.add(value)
    }

    fun makeItLighterAndImmutable() {
        vertexIndicesMap = hashMapOf()
        isAbleToAdd = false
    }

    fun addEdge(
        firstVertexValue: V,
        secondVertexValue: V,
        label: String = "",
        weight: Int = 1,
    ) {
        require(isAbleToAdd) {
            "Not able to add edges when graph is immutable"
        }
        val firstVertexInd =
            vertexIndicesMap[firstVertexValue]
                ?: throw IllegalArgumentException("Graph doesn't have $firstVertexValue vertex")
        val secondVertexInd =
            vertexIndicesMap[secondVertexValue]
                ?: throw IllegalArgumentException("Graph doesn't have $firstVertexValue vertex")
        if (!hasNegativeWeights && weight < 0) {
            hasNegativeWeights = true
        }
        adjacencyList.addEdge(firstVertexInd, secondVertexInd, label, weight)
    }

    abstract fun shortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>?

    abstract fun stronglyConnectedComponents(): ArrayList<ArrayList<Int>>
}
