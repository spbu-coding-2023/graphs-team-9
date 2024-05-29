package graph

import algorithms.CycleSearchAlgorithm
import algorithms.DijkstraAlgorithm
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

    fun shortestPathByDijkstra(
        startVertexValue: String,
        endVertexValue: String,
    ): ArrayList<Int> {
        require(!this.hasNegativeWeights) {
            "Graph must not contain negative edges"
        }
        require(startVertexValue != endVertexValue) {
            "Enter 2 different vertices"
        }
        val algo = DijkstraAlgorithm(this.adjacencyList())
        var startVertexIndex = -1
        var endVertexIndex = -1
        when (isAbleToAdd) {
            true -> {
                try {
                    startVertexIndex = vertexIndicesMap.getValue(startVertexValue)
                } catch (e: NoSuchElementException) {
                    throw NoSuchElementException("There is no vertex $startVertexValue in the graph")
                }
                try {
                    endVertexIndex = vertexIndicesMap.getValue(endVertexValue)
                } catch (e: NoSuchElementException) {
                    throw NoSuchElementException("There is no vertex $endVertexValue in the graph")
                }
            }
            false -> {
                for (vertexIndex in 0 until verticesCount()) {
                    if (vertexValue(vertexIndex) == startVertexValue) startVertexIndex = vertexIndex
                    if (vertexValue(vertexIndex) == endVertexValue) endVertexIndex = vertexIndex
                    break
                }
                require(startVertexIndex != -1) {
                    "There is no vertex $startVertexValue in the graph"
                }
                require(endVertexIndex != -1) {
                    "There is no vertex $endVertexValue in the graph"
                }
            }
        }
        return algo.findShortestPath(startVertexIndex, endVertexIndex)
    }

    fun findCyclesForVertex(vertexValue: String): ArrayList<ArrayList<Int>> {
        var inputVertexIndex = -1
        when (isAbleToAdd) {
            true -> {
                try {
                    inputVertexIndex = vertexIndicesMap.getValue(vertexValue)
                } catch (e: NoSuchElementException) {
                    throw NoSuchElementException("There is no vertex $vertexValue in the graph")
                }
            }
            false -> {
                for (vertexIndex in 0 until verticesCount()) {
                    if (vertexValue(vertexIndex) == vertexValue) inputVertexIndex = vertexIndex
                    break
                }
            }
        }
        val algo = CycleSearchAlgorithm(this.adjacencyList())
        return algo.findCyclesForVertex(inputVertexIndex)
    }

    abstract fun stronglyConnectedComponents(): ArrayList<ArrayList<Int>>

    abstract fun minimumSpanningForest(): Graph

    fun partition(): Map<Int, Int> {
        return getPartition(svsEdgesList(), 1)
    }
}
