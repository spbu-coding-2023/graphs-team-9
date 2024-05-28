package viewModel

import graph.Edge

class DirectedEdgeVM(
    source: VertexVM,
    target: VertexVM,
    edge: Edge,
) : EdgeVM(source, target, edge) {
    override fun shouldColorEdge(
        sourcePathPosition: Int,
        targetPathPosition: Int,
    ): Boolean {
        return targetPathPosition - sourcePathPosition == 1
    }
}
