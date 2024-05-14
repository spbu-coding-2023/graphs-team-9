package graph

abstract class Graph<V> {
    private val vertexValues: ArrayList<V> = arrayListOf()
    protected val adjacencyList: ArrayList<ArrayList<Edge>> = arrayListOf()
    protected var vertexIndicesMap: HashMap<V, Int> = hashMapOf()
    private var verticesCount = 0
    protected var isAbleToAdd = true

    fun getTheAdjacencyList(): ArrayList<ArrayList<Edge>> {
        return adjacencyList
    }

    fun getVertexValues(): ArrayList<V> {
        return vertexValues
    }

    fun getVerticesCount(): Int {
        return verticesCount
    }

    fun addVertex(value: V) {
        if (isAbleToAdd) {
            vertexIndicesMap[value] = verticesCount++
            adjacencyList.add(arrayListOf())
            vertexValues.add(value)
        } else {
            throw IllegalStateException("Not able to add vertices when graph is immutable")
        }
    }

    fun makeItLighterAndImmutable() {
        vertexIndicesMap = hashMapOf()
        isAbleToAdd = false
    }

    fun addEdge(
        firstVertexValue: V,
        secondVertexValue: V,
        weight: Int = 1,
        label: String = "",
    ) {
        if (isAbleToAdd) {
            val firstVertexInd =
                vertexIndicesMap[firstVertexValue]
                    ?: throw IllegalArgumentException("Graph has no $firstVertexValue vertex")
            val secondVertexInd =
                vertexIndicesMap[secondVertexValue]
                    ?: throw IllegalArgumentException("Graph has no $firstVertexValue vertex")
            addEdgeToAdjacencyList(firstVertexInd, secondVertexInd, label, weight)
        } else {
            throw IllegalStateException("Not able to add edges when graph is immutable")
        }
    }

    abstract fun addEdgeToAdjacencyList(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Int,
    )

    abstract fun getShortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>?

    abstract fun getStronglyComponents(): ArrayList<ArrayList<Int>>
}
