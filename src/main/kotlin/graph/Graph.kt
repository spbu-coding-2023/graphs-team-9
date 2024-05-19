package graph

import org.jetbrains.research.ictl.louvain.getPartition

abstract class Graph<V> {
    protected open val vertexValues: ArrayList<V> = arrayListOf()
    protected var vertexIndicesMap: HashMap<V, Int> = hashMapOf()
    protected var isAbleToAdd = true
    protected var hasNegativeWeights = false

    abstract fun adjacencyList(): AdjacencyList

    abstract fun svsEdgesList(): List<SourceVertexStoringEdge>

    fun vertexValue(vertexIndex: Int): V {
        return vertexValues[vertexIndex]
    }

    abstract fun verticesCount(): Int

    abstract fun addVertex(value: V)

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
        addIntoEdgesCollection(firstVertexInd, secondVertexInd, label, weight)
    }

    protected abstract fun addIntoEdgesCollection(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Number,
    )

    abstract fun findBridges(): MutableSet<Set<Int>>

    abstract fun shortestPathByBFAlgorithm(
        start: V,
        end: V,
    ): MutableList<Int>?

    abstract fun stronglyConnectedComponents(): ArrayList<ArrayList<Int>>

    abstract fun minimumSpanningForest(): Graph<V>

    fun partition(): Map<Int, Int> {
        return getPartition(svsEdgesList(), 1)
    }
}
