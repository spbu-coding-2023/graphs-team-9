package graph

import java.io.BufferedWriter
import java.io.FileOutputStream
import java.io.OutputStream

class Write<V>(private val graph: Graph<V>, name: String) {
    private val svsEdgesList = graph.svsEdgesList()
    private val verticesCount = graph.verticesCount()
    private val edgesCount = svsEdgesList.size

    init {
        FileOutputStream("${name}.csv").apply{ writeCsv() }
    }

    private fun OutputStream.writeCsv() {
        val writer = bufferedWriter()
        writeMainData(writer)
        writeVertices(writer)
        writeEdges(writer)
        writer.flush()
    }

    private fun writeMainData(writer: BufferedWriter) {
        val undirectedGraph = UndirectedGraph<V>()
        val directedGraph = DirectedGraph<V>()
        when (graph::class) {
            undirectedGraph::class -> writer.write(""""UndirectedGraph<V>()", """)
            directedGraph::class -> writer.write(""""DirectedGraph<V>()", """)
        }
        writer.write(""""est ves ili net", "$verticesCount", "$edgesCount"""")
        writer.newLine()
    }

    private fun writeVertices(writer: BufferedWriter) {
        for (vertex in 0 until verticesCount) {
            writer.write(""""${graph.vertexValue(vertex)}"""")
            writer.newLine()
        }
    }

    private fun writeEdges(writer: BufferedWriter) {
        for (idEdge in 0 until edgesCount) {
            val edge = svsEdgesList[idEdge]
            writer.write(""""${edge.source()}", "${edge.target()}", "${edge.label()}", "${edge.weight()}"""")
            writer.newLine()
        }
    }
}