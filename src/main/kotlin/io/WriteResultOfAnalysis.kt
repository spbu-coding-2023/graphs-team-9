package io

import graph.DirectedGraph
import graph.UndirectedGraph
import viewModel.DirectedGraphVM
import viewModel.GraphVM
import viewModel.UndirectedGraphVM
import java.io.BufferedWriter
import java.io.FileWriter

class WriteResultOfAnalysis(private val graph: GraphVM, name: String) {
    val vertices = graph.vertices
    val verticesCount = vertices.size
    private val writer = BufferedWriter(FileWriter("graphs/$name.csv"))

    init {
        writeData()
        writer.close()
    }

    private fun writeData() {
        val undirectedGraph = UndirectedGraphVM(UndirectedGraph())
        val directedGraph = DirectedGraphVM(DirectedGraph())
        writer.write("Analyzed, ")
        when (graph::class) {
            undirectedGraph::class -> writer.write("Undirected, ")
            directedGraph::class -> writer.write("Directed, ")
        }
        writer.write("$verticesCount")

        writeVertices()
        writeEdges()
    }

    private fun writeVertices() {
        for (vertex in vertices) {
            writer.newLine()
            writer.write(
                "${vertex.data}, ${vertex.x.toString().substringBeforeLast(".")}, " +
                    "${vertex.y.toString().substringBeforeLast(".")}, " +
                    "${vertex.size.toString().substringBeforeLast(".")}\n${vertex.color.red}, " +
                    "${vertex.color.green}, ${vertex.color.blue}",
            )
        }
    }

    private fun writeEdges() {
        for (edge in graph.edges) {
            writer.newLine()
            writer.write(
                "${edge.source.data}, ${edge.target.data}, ${edge.edge.label()}, ${edge.edge.weight()}\n" +
                    "${edge.color.red}, ${edge.color.green}, ${edge.color.blue}",
            )
        }
    }
}
