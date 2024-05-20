package graph

import java.io.BufferedWriter
import java.io.FileWriter

class Write<V>(private val graph: Graph<V>, private val name: String) {
    private val verticesCount = graph.verticesCount()
    private val writer = BufferedWriter(FileWriter("$name.csv"))

    init {
        writeData()
        writer.close()
    }

    private fun writeData() {
        val undirectedGraph = UndirectedGraph<V>()
        val directedGraph = DirectedGraph<V>()
        var isGraphDirected = false
        when (graph::class) {
            undirectedGraph::class -> {
                writer.write("Undirected, ")
                writer.write("$verticesCount, ${graph.svsEdgesList().size}, ")
            }
            directedGraph::class -> {
                writer.write("Directed, ")
                var edgesCount = 0
                val adjacencyList = graph.adjacencyList()
                for (vertex in 0 until verticesCount) edgesCount += adjacencyList.outgoingEdgesCount(vertex)
                writer.write("$verticesCount, $edgesCount, ")
                isGraphDirected = true
            }
        }
        when (graph.isWeighted()) {
            true -> writer.write("Weighted")
            else -> writer.write("Unweighted")
        }
        writer.newLine()

        writeVertices()
        writeEdges(isGraphDirected)
    }

    private fun writeVertices() {
        for (vertex in 0 until verticesCount) {
            writer.write("${graph.vertexValue(vertex)}")
            writer.newLine()
        }
    }

    private fun writeEdges(isGraphDirected: Boolean) {
        when (isGraphDirected) {
            true -> {
                val adjacencyList = graph.adjacencyList()
                for (vertex in 0 until verticesCount) {
                    for (idEdge in 0 until adjacencyList.outgoingEdgesCount(vertex)) {
                        val edge = adjacencyList.getEdge(vertex, idEdge)
                        writer.write("${vertex}, ${edge.target()}, ${edge.label()}, ${edge.weight()}")
                        writer.newLine()
                    }
                }
            }
            else -> {
                val svsEdgesList = graph.svsEdgesList()
                for (edge in svsEdgesList) {
                    writer.write("${edge.source()}, ${edge.target()}, ${edge.label()}, ${edge.weight()}")
                    writer.newLine()
                }
            }
        }
    }
}