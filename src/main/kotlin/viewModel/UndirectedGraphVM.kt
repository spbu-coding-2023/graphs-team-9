package viewModel

import graph.Graph
import graph.UndirectedGraph

class UndirectedGraphVM(
    graph: UndirectedGraph,
) : GraphVM(graph) {
    override val edges : List<UndirectedEdgeVM>
        init {
            this.stronglyConnectedComponentsAvailability = false
            val edges = arrayListOf<UndirectedEdgeVM>()
            graph.svsEdgesList().forEach { edges.add(UndirectedEdgeVM(vertices[it.source()], vertices[it.target()], it)) }
            this.edges = edges
        }
}
