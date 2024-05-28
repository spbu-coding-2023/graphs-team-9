package graph

import algorithms.layout.ForceAtlas2
import org.jetbrains.research.ictl.louvain.getPartition

abstract class Graph {
    protected open val vertexValues: ArrayList<String> = arrayListOf()
    protected var vertexIndicesMap: HashMap<String, Int> = hashMapOf()
    protected var isAbleToAdd = true
    protected var hasNegativeWeights = false
    internal var weighted: Boolean = false

    abstract fun adjacencyList(): AdjacencyList

    abstract fun svsEdgesList(): List<SourceVertexStoringEdge>

    fun vertexValue(vertexIndex: Int): String {
        require(vertexIndex < vertexValues.size && 0 <= vertexIndex) {
            "Vertex with index $vertexIndex doesn't exist"
        }
        return vertexValues[vertexIndex]
    }

    abstract fun verticesCount(): Int

    abstract fun addVertex(value: String)

    fun isWeighted(): Boolean = weighted

    fun makeItLighterAndImmutable() {
        vertexIndicesMap = hashMapOf()
        isAbleToAdd = false
    }

    fun addEdge(
        firstVertexValue: String,
        secondVertexValue: String,
        label: String = "",
        weight: Double = 1.0,
    ) {
        require(isAbleToAdd) {
            "Not able to add edges when graph is immutable"
        }
        val firstVertexInd =
            vertexIndicesMap[firstVertexValue]
                ?: throw IllegalArgumentException("Graph doesn't have $firstVertexValue vertex")
        val secondVertexInd =
            vertexIndicesMap[secondVertexValue]
                ?: throw IllegalArgumentException("Graph doesn't have $secondVertexValue vertex")
        if (!hasNegativeWeights && weight < 0) {
            hasNegativeWeights = true
        }
        addIntoEdgesCollection(firstVertexInd, secondVertexInd, label, weight)
    }

    protected abstract fun addIntoEdgesCollection(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Double,
    )

    abstract fun findBridges(): MutableSet<Set<Int>>

    abstract fun shortestPathByBFAlgorithm(
        start: String,
        end: String,
    ): MutableList<Int>?


    fun layout(
        iterations: Int = 100,
        randomStartPositionsMode: Boolean = false,
        scalingRatio: Double = 2.0,
        gravity: Double = 1.0,
        strongGravityMode: Boolean = true,
        edgeWeightInfluence: Double = 1.0,
    ): List<Pair<Double, Double>> {
        val algo = ForceAtlas2(this.adjacencyList())
        return algo.layout(iterations, randomStartPositionsMode, scalingRatio, gravity, strongGravityMode, edgeWeightInfluence)
    }

    abstract fun stronglyConnectedComponents(): ArrayList<ArrayList<Int>>

    abstract fun minimumSpanningForest(): Graph

    fun partition(): Map<Int, Int> {
        return getPartition(svsEdgesList(), 1)
    }
}
