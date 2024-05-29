package graph

import algorithms.BellmanFordAlgorithm
import algorithms.BoruvkaSAlgorithm
import algorithms.ExtremelyFastAlgorithm
import algorithms.FindBridgesAlgorithm

open class UndirectedGraph() : Graph() {
    private var verticesCount: Int = 0
    private var svsEdgesList: ArrayList<SourceVertexStoringEdge> = arrayListOf()
    override var vertexValues: ArrayList<String> = arrayListOf()

    override fun svsEdgesList(): List<SourceVertexStoringEdge> {
        return svsEdgesList
    }

    override fun adjacencyList(): UndirectedAdjacencyList {
        val adjacencyList = UndirectedAdjacencyList(verticesCount)
        for (svsEdge in svsEdgesList()) {
            adjacencyList.addEdge(svsEdge.source(), svsEdge.target(), svsEdge.label(), svsEdge.weight())
        }
        return adjacencyList
    }

    override fun verticesCount(): Int {
        return verticesCount
    }

    override fun addIntoEdgesCollection(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Double,
    ) {
        require(!isEdgeContained(firstVertexInd, secondVertexInd)) {
            "Duplicated edges are not allowed"
        }
        svsEdgesList.add(
            SourceVertexStoringEdge(
                firstVertexInd,
                secondVertexInd,
                label,
                weight,
            ),
        )
    }

    private fun isEdgeContained(
        source: Int,
        target: Int,
    ): Boolean {
        for (edge in svsEdgesList()) {
            if (setOf(edge.source(), edge.target()) == setOf(source, target)) {
                return true
            }
        }
        return false
    }

    override fun addVertex(value: String) {
        require(isAbleToAdd) {
            "Not able to add vertices when graph is immutable"
        }
        if (vertexIndicesMap[value] == null) {
            vertexIndicesMap[value] = verticesCount++
            vertexValues.add(value)
        }
    }

    override fun findBridges(): MutableSet<Set<Int>> {
        val algo = FindBridgesAlgorithm(adjacencyList())
        return algo.findBridges()
    }

    override fun shortestPathByBFAlgorithm(
        start: String,
        end: String,
    ): MutableList<Int>? {
        if (hasNegativeWeights) throw UnsupportedOperationException("getStronglyComponent() hasn't implemented for undirected graphs")

        val algo = BellmanFordAlgorithm(adjacencyList())
        var idStart = -1
        var idEnd = -1
        when (isAbleToAdd) {
            true -> {
                if (vertexIndicesMap[start] == null || vertexIndicesMap[end] == null) {
                    throw IllegalArgumentException(
                        "Vertices can not be null",
                    )
                }
                idStart = vertexIndicesMap.getValue(start)
                idEnd = vertexIndicesMap.getValue(end)
            }
            false -> {
                for (i in 0 until verticesCount()) {
                    if (vertexValue(i) == start) idStart = i
                    if (vertexValue(i) == end) idEnd = i
                }
                if (idStart == -1 || idEnd == -1) throw IllegalArgumentException("Vertices can not be null")
            }
        }
        return algo.findPath(idStart, idEnd)
    }

    override fun stronglyConnectedComponents(): ArrayList<ArrayList<Int>> {
        throw UnsupportedOperationException("getStronglyComponent() hasn't implemented for undirected graphs")
    }

    override fun minimumSpanningForest(): UndirectedGraph {
        val boruvkaSAlgorithm = BoruvkaSAlgorithm(svsEdgesList, verticesCount)
        return UndirectedGraph(boruvkaSAlgorithm.boruvkaSAlgo(), vertexValues)
    }

    override fun keyVertices(): DoubleArray = ExtremelyFastAlgorithm(adjacencyList()).getKeyVertices()

    internal constructor(svsEdgesList: ArrayList<SourceVertexStoringEdge>, vertexValues: ArrayList<String>) : this() {
        this.svsEdgesList = svsEdgesList
        this.verticesCount = vertexValues.size

        for (edge in svsEdgesList) {
            require(0 <= edge.source() && edge.source() < this.verticesCount) {
                "graph doesn't have ${edge.source()} vertex"
            }
            require(0 <= edge.target() && edge.target() < this.verticesCount) {
                "graph doesn't have ${edge.target()} vertex"
            }
        }

        this.vertexValues = vertexValues
    }
}
