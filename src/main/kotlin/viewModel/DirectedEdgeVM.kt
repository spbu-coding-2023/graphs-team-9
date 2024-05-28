package viewModel

import androidx.compose.ui.graphics.Color
import graph.Edge

class DirectedEdgeVM(
    source: VertexVM,
    target: VertexVM,
    edge: Edge,
    basicColor: Color = Color.Black
) : EdgeVM(source, target, edge, basicColor) {
    override fun shouldColorEdge(
        sourcePathPosition: Int,
        targetPathPosition: Int,
    ): Boolean {
        return targetPathPosition - sourcePathPosition == 1
    }
}
