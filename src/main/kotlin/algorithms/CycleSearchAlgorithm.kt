package algorithms

import graph.AdjacencyList

class CycleSearchAlgorithm(private val adjacencyList: AdjacencyList) {
    private val vertexCount = adjacencyList.verticesCount()
    private val cyclesArray = ArrayList<ArrayList<Int>>()

    enum class VertexStatus {
        NOT_VISITED,
        PARTITION_VISITED,
        VISITED,
    }

    fun findCyclesForVertex(vertexIndex: Int): ArrayList<ArrayList<Int>> {
        val visitedStatusArray = Array(vertexCount) { VertexStatus.NOT_VISITED }
        val parentVertexArray = IntArray(vertexCount)
        findCyclesWithDFS(vertexIndex, -1, visitedStatusArray, parentVertexArray, vertexIndex)
        return cyclesArray
    }

    private fun findCyclesWithDFS(
        currentVertexIndex: Int,
        parentVertexIndex: Int,
        visitedStatusArray: Array<VertexStatus>,
        parentArray: IntArray,
        vertexIndex: Int,
    ) {
        if (visitedStatusArray[currentVertexIndex] == VertexStatus.VISITED) {
            return
        }
        if (visitedStatusArray[currentVertexIndex] == VertexStatus.PARTITION_VISITED) {
            val currentCycleArray = ArrayList<Int>()
            var currentVertexInCycleIndex = parentVertexIndex
            currentCycleArray.add(currentVertexInCycleIndex)
            while (currentVertexInCycleIndex != currentVertexIndex) {
                currentVertexInCycleIndex = parentArray[currentVertexInCycleIndex]
                currentCycleArray.add(currentVertexInCycleIndex)
            }
            if (vertexIndex !in currentCycleArray) return
            currentCycleArray.reverse()
            cyclesArray.add(currentCycleArray)
            return
        }
        parentArray[currentVertexIndex] = parentVertexIndex
        visitedStatusArray[currentVertexIndex] = VertexStatus.PARTITION_VISITED
        for (outgoingEdgeNumber in 0 until adjacencyList.outgoingEdgesCount(currentVertexIndex)) {
            val nextVertexIndex = adjacencyList.getEdge(currentVertexIndex, outgoingEdgeNumber).target()
            if (nextVertexIndex == parentArray[currentVertexIndex]) {
                continue
            }
            findCyclesWithDFS(nextVertexIndex, currentVertexIndex, visitedStatusArray, parentArray, vertexIndex)
        }
        visitedStatusArray[currentVertexIndex] = VertexStatus.VISITED
    }
}
