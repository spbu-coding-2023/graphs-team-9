package algorithms

import graph.AdjacencyList
import kotlin.math.ceil
import kotlin.math.sqrt

class ExtremelyFastAlgorithm(private val adjacencyList: AdjacencyList) {
    private val verticesCount = adjacencyList.verticesCount()
    private val basis: Int = ceil(sqrt(verticesCount.toDouble())).toInt()
    private val groups: Int = 3 * basis
    private val closenessCentrality = DoubleArray(groups)
    private val arrayOfSuperVertices: MutableList<List<Int>> = mutableListOf()
    private val result = DoubleArray(verticesCount) { 0.0 }

    fun getKeyVertices(): DoubleArray {
        buildTable()
        for (idGroup in 0 until groups) {
            for (vertex in arrayOfSuperVertices[idGroup]) result[vertex] += closenessCentrality[idGroup]
        }
        return result
    }

    private fun buildTable() {
        for (idGroup in 0 until groups) {
            val superVertex = buildSuperVertex(idGroup)
            arrayOfSuperVertices.add(superVertex.toList())
            val cc = calculateCC(superVertex)
            closenessCentrality[idGroup] = cc
        }
    }

    private fun buildSuperVertex(idTest: Int): MutableList<Int> {
        val superVertex: MutableList<Int> = mutableListOf()
        var vertexToAdd = -1
        for (i in 0 until basis) {
            when (idTest) {
                in 0 until basis -> vertexToAdd = idTest * basis + i
                in basis until 2 * basis -> vertexToAdd = idTest + basis * (i - 1)
                in 2 * basis until 3 * basis -> vertexToAdd = (idTest + basis - i) % basis + i * basis
            }
            if (vertexToAdd >= verticesCount) break
            superVertex.add(vertexToAdd)
        }
        return superVertex
    }

    private fun calculateCC(superVertex: MutableList<Int>): Double {
        var ccForGroup = 0L
        val ccForVertex = LongArray(verticesCount) { 0 }
        val visited = BooleanArray(verticesCount) { false }
        for (vertex in superVertex) visited[vertex] = true
        while (superVertex.isNotEmpty()) {
            val neighbours: MutableList<Int> = mutableListOf()
            for (vertex in superVertex) {
                for (idEdge in 0 until adjacencyList.outgoingEdgesCount(vertex)) {
                    val neighbour = adjacencyList.getEdge(vertex, idEdge).target()
                    if (!visited[neighbour]) {
                        visited[neighbour] = true
                        neighbours.add(neighbour)
                        ccForVertex[neighbour] = ccForVertex[vertex] + 1
                        ccForGroup += ccForVertex[neighbour]
                    }
                }
            }
            superVertex.clear()
            for (el in neighbours) superVertex.add(el)
        }
        return when (ccForGroup) {
            0L -> 0.0
            else -> (1 / ccForGroup.toDouble())
        }
    }
}
