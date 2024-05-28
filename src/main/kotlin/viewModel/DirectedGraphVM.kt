package viewModel

import graph.DirectedGraph

class DirectedGraphVM(
    graph: DirectedGraph,
) : GraphVM(graph) {
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
