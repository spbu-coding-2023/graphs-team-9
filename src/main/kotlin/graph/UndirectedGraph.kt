package graph

import algorithms.BellmanFordAlgorithm
import algorithms.FindBridgesAlgorithm

class UndirectedGraph<V>(
    private val svsEdgesList: ArrayList<SourceVertexStoringEdge> = arrayListOf(),
    override val vertexValues: ArrayList<V> = arrayListOf(),
) : Graph<V>() {

    init {
        require(svsEdgesList.size == vertexValues.size) {
            "vertexValues size isn't equal adjacencyList's vertices count"
        }
    }

    private var verticesCount: Int = 0

    fun svsEdgesList(): List<SourceVertexStoringEdge> {
        return svsEdgesList.toList()
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
        weight: Number,
    ) {
        require(!isEdgeContained(firstVertexInd, secondVertexInd)) {
            "Duplicated edges are not allowed"
        }
        svsEdgesList.add(
            SourceVertexStoringEdge(
                firstVertexInd,
                secondVertexInd,
                label,
                weight.toDouble(),
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

    override fun addVertex(value: V) {
        require(isAbleToAdd) {
            "Not able to add vertices when graph is immutable"
        }
        if (vertexIndicesMap[value] == null) {
            vertexIndicesMap[value] = verticesCount++
        }
        vertexValues.add(value)
    }

    override fun findBridges(): MutableSet<Set<Int>> {
        val algo = FindBridgesAlgorithm(adjacencyList)
        return algo.findBridges()
    }

    override fun shortestPathByBFAlgorithm(
        start: V,
        end: V,
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
}
