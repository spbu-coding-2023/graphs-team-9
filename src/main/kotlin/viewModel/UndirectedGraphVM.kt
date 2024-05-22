package viewModel

import graph.Graph

class UndirectedGraphVM(
    graph: Graph,
) : GraphVM(graph) {
        init {
            this.stronglyConnectedComponentsAvailability = false
        }
            override val edges = List(graph.svsEdgesList().size){i ->
                val edge = graph.svsEdgesList()[i]
                UndirectedEdgeVM(vertices[edge.source()], vertices[edge.target()], edge)}
}