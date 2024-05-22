package viewModel

import graph.Edge
import kotlin.math.absoluteValue

class UndirectedEdgeVM(
        source: VertexVM,
        target: VertexVM,
        edge: Edge,
    ) : EdgeVM(source, target, edge) {
    override fun shouldColorEdge(sourcePathPosition: Int, targetPathPosition: Int): Boolean {
        return (sourcePathPosition - targetPathPosition).absoluteValue == 1
    }
}