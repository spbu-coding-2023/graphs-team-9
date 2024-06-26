package viewModel

import graph.Graph
import graph.UndirectedGraph
import io.Read
import io.ReadResultOfAnalysis
import view.Screen
import java.io.BufferedReader
import java.io.FileReader

class FileListVM(private val file: String) {
    fun getGraph(): Graph {
        try {
            return Read("graphs/$file").getGraph()
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }
    }

    fun getGraphVM(): GraphVM {
        try {
            return ReadResultOfAnalysis("graphs/$file").getGraph()
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }
    }

    fun defineGraphType(graph: Graph): Screen {
        val undirectedGraph = UndirectedGraph()
        return when (graph::class) {
            undirectedGraph::class -> Screen.UndirectedGraph
            else -> Screen.DirectedGraph
        }
    }

    fun defineGraphVMType(graph: GraphVM): Screen {
        val undirectedGraph = UndirectedGraphVM(UndirectedGraph())
        return when (graph::class) {
            undirectedGraph::class -> Screen.UndirectedGraph
            else -> Screen.DirectedGraph
        }
    }

    fun isGraphAnalyzed(): Boolean {
        try {
            val reader = BufferedReader(FileReader("graphs/$file"))
            val mainData = reader.readLine().split(", ")
            reader.close()
            return when (mainData[0]) {
                "Analyzed" -> true
                else -> false
            }
        } catch (e: Exception) {
            throw IllegalArgumentException()
        }
    }
}
