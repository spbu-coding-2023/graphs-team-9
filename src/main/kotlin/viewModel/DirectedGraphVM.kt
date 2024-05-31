package viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import graph.Graph

class DirectedGraphVM(
    graph: Graph,
    verticesColors: ArrayList<Color>? = null,
    coordinates: ArrayList<Pair<Double, Double>>? = null,
    sizes: ArrayList<Dp>? = null,
    edgesColors: HashMap<Pair<String, String>, Color>? = null,
) : GraphVM(graph, verticesColors, coordinates, sizes, edgesColors) {
    override val edges: List<DirectedEdgeVM>

    init {
        this.mfsAvailability = false
        this.bridgesAvailability = false
        val edges = arrayListOf<DirectedEdgeVM>()
        for (source in 0 until graph.verticesCount()) {
            for (outgoingEdge in 0 until graph.adjacencyList().outgoingEdgesCount(source)) {
                val edge = graph.adjacencyList().getEdge(source, outgoingEdge)
                edges.add(DirectedEdgeVM(vertices[source], vertices[edge.target()], edge))
            }
        }
        this.edges = edges
    }
}
