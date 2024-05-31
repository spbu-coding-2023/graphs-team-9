package io

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graph.DirectedGraph
import graph.Graph
import graph.UndirectedGraph
import viewModel.DirectedGraphVM
import viewModel.GraphVM
import viewModel.UndirectedGraphVM
import java.io.BufferedReader
import java.io.FileReader

class ReadResultOfAnalysis(name: String) {
    private val reader = BufferedReader(FileReader(name))
    private val verticesColors: ArrayList<Color> = arrayListOf()
    private val coordinates: ArrayList<Pair<Double, Double>> = arrayListOf()
    private val sizes: ArrayList<Dp> = arrayListOf()
    private val edgesColors: HashMap<Pair<String, String>, Color> = hashMapOf()
    private var isGraphDirected = false
    private lateinit var graphVM: GraphVM

    fun getGraph(): GraphVM {
        val graph = readData()
        reader.close()

        graphVM =
            when (isGraphDirected) {
                true -> DirectedGraphVM(graph, verticesColors, coordinates, sizes, edgesColors)
                else -> UndirectedGraphVM(graph, verticesColors, coordinates, sizes, edgesColors)
            }
        graphVM.partitionAvailability = false
        graphVM.keyVerticesAvailability = false
        graphVM.mfsAvailability = false
        graphVM.shortestPathAvailability = false
        graphVM.stronglyConnectedComponentsAvailability = false
        graphVM.cyclesAvailability = false
        graphVM.bridgesAvailability = false
        return graphVM
    }

    private fun readData(): Graph {
        val (isAnalyzed, type, verticesCount) = reader.readLine().split(", ")
        val graph = createGraph(type)

        readVertices(graph, verticesCount.toInt())
        readEdges(graph)
        return graph
    }

    private fun createGraph(type: String): Graph {
        when (type) {
            "Undirected" -> {
                val graph = UndirectedGraph()
                return graph
            }
            else -> {
                isGraphDirected = true
                val graph = DirectedGraph()
                return graph
            }
        }
    }

    private fun readVertices(
        graph: Graph,
        verticesCount: Int,
    ) {
        for (vertex in 0 until verticesCount) {
            val (name, x, y, size) = reader.readLine().split(", ")
            val (r, g, b) = reader.readLine().split(", ")

            verticesColors.add(Color(r.toFloat(), g.toFloat(), b.toFloat()))
            coordinates.add((x.toDouble()) to (y.toDouble()))
            sizes.add(size.toDouble().dp)
            graph.addVertex(name)
        }
    }

    private fun readEdges(graph: Graph) {
        var line = reader.readLine()
        while (line != null) {
            val (source, target, label, weight) = line.split(", ")
            val (r, g, b) = reader.readLine().split(", ")
            graph.addEdge(source, target, label, weight.toDouble())
            edgesColors.put(Pair(source, target), Color(r.toFloat(), g.toFloat(), b.toFloat()))
            line = reader.readLine()
        }
    }
}
