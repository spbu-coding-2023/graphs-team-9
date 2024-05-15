package graph

abstract class Graph<V> {
    private val vertexValues: ArrayList<V> = arrayListOf()
    protected abstract val adjacencyList: AdjacencyList
    protected var vertexIndicesMap: HashMap<V, Int> = hashMapOf()
    protected var isAbleToAdd = true
    protected var hasNegativeWeights = false

    open fun getTheAdjacencyList(): AdjacencyList {
        return adjacencyList
    }

    fun getVertexValue(vertexIndex: Int): V {
        return vertexValues[vertexIndex]
    }

    fun getVerticesCount(): Int {
        return adjacencyList.getVerticesCount()
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

    abstract fun getShortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>?

    abstract fun getStronglyComponents(): ArrayList<ArrayList<Int>>
}
