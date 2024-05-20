package graph

import java.io.BufferedWriter
import java.io.FileWriter

class Write<V>(private val graph: Graph<V>, name: String) {
    private val svsEdgesList = graph.svsEdgesList()
    private val verticesCount = graph.verticesCount()
    private val edgesCount = svsEdgesList.size
    private val writer = BufferedWriter(FileWriter("$name.csv"))

    init {
        writeMainData()
        writeVertices()
        writeEdges()
        writer.close()
    }

    private fun writeMainData() {
        val undirectedGraph = UndirectedGraph<V>()
        val directedGraph = DirectedGraph<V>()
        when (graph::class) {
            undirectedGraph::class -> writer.write("Undirected, ")
            directedGraph::class -> writer.write("Directed, ")
        }
        writer.write("est ves ili net, $verticesCount, $edgesCount")
        writer.newLine()
    }

    private fun writeVertices() {
        for (vertex in 0 until verticesCount) {
            writer.write("${graph.vertexValue(vertex)}")
            writer.newLine()
        }
    }

    private fun writeEdges() {
        for (idEdge in 0 until edgesCount) {
            val edge = svsEdgesList[idEdge]
            writer.write("${edge.source()}, ${edge.target()}, ${edge.label()}, ${edge.weight()}")
            writer.newLine()
        }
    }
}