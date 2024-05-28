package io

import graph.DirectedGraph
import graph.Graph
import graph.UndirectedGraph
import java.io.BufferedWriter
import java.io.FileWriter

class Write(private val graph: Graph, name: String) {
    private val verticesCount = graph.verticesCount()
    private val isGraphWeighted = graph.isWeighted()
    private val writer = BufferedWriter(FileWriter("$name.csv"))

    init {
        writeData()
        writer.close()
    }

    private fun writeData() {
        val undirectedGraph = UndirectedGraph()
        val directedGraph = DirectedGraph()
        var isGraphDirected = false
        when (graph::class) {
            undirectedGraph::class -> writer.write("Undirected, ")
            directedGraph::class -> {
                writer.write("Directed, ")
                isGraphDirected = true
            }
        }
        when (isGraphWeighted) {
            true -> writer.write("Weighted, ")
            else -> writer.write("Unweighted, ")
        }
        writer.write("$verticesCount")

        writeVertices()
        writeEdges(isGraphDirected)
    }

    private fun writeVertices() {
        for (vertex in 0 until verticesCount) {
            writer.newLine()
            writer.write(graph.vertexValue(vertex))
        }
    }

    private fun writeEdges(
        isGraphDirected: Boolean,
    ) {
        when (isGraphDirected) {
            true -> {
                val adjacencyList = graph.adjacencyList()
                for (vertex in 0 until verticesCount) {
                    for (idEdge in 0 until adjacencyList.outgoingEdgesCount(vertex)) {
                        val edge = adjacencyList.getEdge(vertex, idEdge)
                        writer.newLine()
                        writer.write("${graph.vertexValue(vertex)}, ${graph.vertexValue(edge.target())}, ${edge.label()}")
                        if (isGraphWeighted) writer.write(", ${edge.weight()}")
                    }
                }
            }
            else -> {
                val svsEdgesList = graph.svsEdgesList()
                for (edge in svsEdgesList) {
                    writer.newLine()
                    writer.write("${graph.vertexValue(edge.source())}, ${graph.vertexValue(edge.target())}, ${edge.label()}")
                    if (isGraphWeighted) writer.write(", ${edge.weight()}")
                }
            }
        }
    }
}
