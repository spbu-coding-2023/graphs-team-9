package graph

import algorithms.BellmanFordAlgorithm
import algorithms.TarjanSAlgo

open class DirectedGraph(
    private val adjacencyList: DirectedAdjacencyList = DirectedAdjacencyList(),
    final override val vertexValues: ArrayList<String> = arrayListOf(),
) : Graph() {
    init {
        require(adjacencyList.verticesCount() == vertexValues.size) {
            "vertexValues size isn't equal adjacencyList's vertices count"
        }
    }

    override fun adjacencyList(): DirectedAdjacencyList {
        return adjacencyList
    }

    override fun svsEdgesList(): List<SourceVertexStoringEdge> {
        val svsEdgesList = ArrayList<SourceVertexStoringEdge>()
        for (vertex in 0 until adjacencyList.verticesCount()) {
            for (outgoingEdgeSInd in 0 until adjacencyList.outgoingEdgesCount(vertex)) {
                val edge = adjacencyList.getEdge(vertex, outgoingEdgeSInd)
                svsEdgesList.add(SourceVertexStoringEdge(vertex, edge.target(), edge.label(), edge.weight()))
            }
        }
        return svsEdgesList
    }

    override fun verticesCount(): Int {
        return adjacencyList.verticesCount()
    }

    override fun addIntoEdgesCollection(
        firstVertexInd: Int,
        secondVertexInd: Int,
        label: String,
        weight: Double,
    ) {
        adjacencyList.addEdge(firstVertexInd, secondVertexInd, label, weight)
    }

    override fun addVertex(value: String) {
        require(isAbleToAdd) {
            "Not able to add vertices when graph is immutable"
        }
        if (vertexIndicesMap[value] == null) {
            vertexIndicesMap[value] = adjacencyList.addVertex()
            vertexValues.add(value)
        }
    }

    override fun findBridges(): MutableSet<Set<Int>> {
        throw UnsupportedOperationException("findBridges() hasn't implemented for directed graphs")
    }

    override fun shortestPathByBFAlgorithm(
        start: String,
        end: String,
    ): MutableList<Int>? {
        val algo = BellmanFordAlgorithm(adjacencyList)
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
        val tarjanSAlgo = TarjanSAlgo(this.adjacencyList())
        return tarjanSAlgo.tarjanSAlgo()
    }

    override fun minimumSpanningForest(): DirectedGraph {
        throw UnsupportedOperationException("minimumSpanningForest() hasn't implemented for directed graphs")
    }

    override fun keyVertices(): DoubleArray {
        throw UnsupportedOperationException("keyVertices() hasn't implemented for directed graphs")
    }
}
