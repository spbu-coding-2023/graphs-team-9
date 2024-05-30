package viewModel

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import graph.Graph
import graph.UndirectedGraph

class UndirectedGraphVM(
    graph: Graph,
    verticesColors: ArrayList<Color>? = null,
    coordinates: ArrayList<Pair<Double, Double>>? = null,
    sizes: ArrayList<Dp>? = null,
    edgesColors: HashMap<Pair<String, String>, Color>? = null,
) : GraphVM(graph, verticesColors, coordinates, sizes, edgesColors) {
    override val edges: List<UndirectedEdgeVM>

    init {
        this.stronglyConnectedComponentsAvailability = false
        val edges = arrayListOf<UndirectedEdgeVM>()
        if (edgesColors == null) {
            graph.svsEdgesList()
                .forEach { edges.add(UndirectedEdgeVM(vertices[it.source()], vertices[it.target()], it)) }
        } else {
            graph.svsEdgesList()
                .forEach {
                    edges.add(
                        UndirectedEdgeVM(
                            vertices[it.source()],
                            vertices[it.target()],
                            it,
                            edgesColors[vertices[it.source()].data to vertices[it.target()].data] ?: Color.Black,
                        ),
                    )
                }
        }
        this.edges = edges
    }
}
