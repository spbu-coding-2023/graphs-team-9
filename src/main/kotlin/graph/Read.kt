package graph

import java.io.BufferedReader
import java.io.FileReader

class Read(name: String) {
    private val reader = BufferedReader(FileReader("$name.csv"))
    init {
        readMainData()
        reader.close()
    }

    private fun readMainData(): Graph<String> {
        val (type, weight, verticesCount, edgesCount) = reader.readLine().split(',')
        val graph = createGraph(type)
        if (weight == "Weighted") graph.weighted = true
        readVertices(graph, verticesCount.toInt())
        readEdges(graph, edgesCount.toInt())
        return graph
    }

    private fun readVertices(graph: Graph<String>, verticesCount: Int) {
        for (vertex in 0 until verticesCount) {
            graph.addVertex(reader.readLine())
        }
    }

    private fun readEdges(graph: Graph<String>, edgesCount: Int) {
        for (vertex in 0 until edgesCount) {
            val (source, target, label, weight) = reader.readLine().split(',')
            graph.addEdge(source, target, label, weight.toInt())
        }
    }

    private fun createGraph(type: String): Graph<String> {
        when (type) {
            "Undirected" -> {
                val graph = UndirectedGraph<String>()
                return graph
            }
            else -> {
                val graph = DirectedGraph<String>()
                return graph
            }
        }
    }
}