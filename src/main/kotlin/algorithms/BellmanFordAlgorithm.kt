package algorithms

import graph.DirectedGraph
import java.lang.UnsupportedOperationException
import kotlin.Int.Companion.MAX_VALUE

class BellmanFordAlgorithm<V>(graph: DirectedGraph<V>) {
    private val verticesCount = graph.getVerticesCount()
    private val vertexValues = graph.getVertexValues()
    private val adjacencyList = graph.getTheAdjacencyList()
    private val pathLengthTable: MutableList<Int> = mutableListOf()
    private val parentsTable: MutableList<Int> = mutableListOf()

    private fun buildTables(start: Int) {
        for (i in 0 ..< verticesCount) {
            pathLengthTable.add(MAX_VALUE)
            parentsTable.add(-1)
        }
        pathLengthTable[start] = 0
        for (iteration in 0 ..< verticesCount) {
            var isTableChanges: Boolean = false
            for (vertex in 0..<verticesCount) {
                val id = (start + vertex) % verticesCount
                if (pathLengthTable[id] != MAX_VALUE) {
                    for (edge in adjacencyList[id]) {
                        val destination = edge.destinationVertexIndex
                        if (pathLengthTable[destination] > pathLengthTable[id] + edge.weight.toInt()) {
                            pathLengthTable[destination] = pathLengthTable[id] + edge.weight.toInt()
                            parentsTable[destination] = id
                            isTableChanges = true
                        }
                    }
                }
            }
            if (!isTableChanges) break
            else if (iteration == verticesCount - 1) throw UnsupportedOperationException("Graph contains negative weight cycle")
        }
    }

    private fun convertIntToCustomType(path: MutableList<Int>?): MutableList<V>? {
        val convertedPath: MutableList<V> = mutableListOf()
        if (path == null) return null
        for (i in path.size - 1 downTo 0) convertedPath.add(vertexValues[path[i]])
        return convertedPath
    }

     fun findPath(start: Int, end: Int): MutableList<V>? {
         buildTables(start)
         val path: MutableList<Int> = mutableListOf()
         var currentVertex = end
         path.add(currentVertex)
         if (start == end) return convertIntToCustomType(path)
         if (parentsTable[end] == -1) return null
         while (parentsTable[currentVertex] != -1){
             path.add(parentsTable[currentVertex])
             currentVertex = parentsTable[currentVertex]
         }
         return convertIntToCustomType(path)
    }
}
