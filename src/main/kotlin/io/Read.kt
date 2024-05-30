package io

import graph.DirectedGraph
import graph.Graph
import graph.UndirectedGraph
import java.io.BufferedReader
import java.io.FileReader

class Read(name: String) {
    private val reader = BufferedReader(FileReader(name))

    fun getGraph(): Graph {
        val graph = readData()
        reader.close()
        return graph
    }

    private fun readData(): Graph {
        val (isAnalyzed, type, weight, verticesCount) = reader.readLine().split(", ")
        val graph = createGraph(type)
        if (weight == "Weighted") graph.weighted = true
        val isGraphWeighted = graph.isWeighted()

        readVertices(graph, verticesCount.toInt())
        readEdges(graph, isGraphWeighted)
        return graph
    }

    private fun readVertices(
        graph: Graph,
        verticesCount: Int,
    ) {
        for (vertex in 0 until verticesCount) {
            graph.addVertex(reader.readLine())
        }
    }

    private fun readEdges(
        graph: Graph,
        isGraphWeighted: Boolean,
    ) {
        var line = reader.readLine()
        while (line != null) {
            when (isGraphWeighted) {
                true -> {
                    val (source, target, label, weight) = line.split(", ")
                    graph.addEdge(source, target, label, weight.toDouble())
                }
                else -> {
                    val (source, target, label) = line.split(", ")
                    graph.addEdge(source, target, label)
                }
            }
            line = reader.readLine()
        }
    }

    private fun createGraph(type: String): Graph {
        when (type) {
            "Undirected" -> {
                val graph = UndirectedGraph()
                return graph
            }
            else -> {
                val graph = DirectedGraph()
                return graph
            }
        }
    }
}
