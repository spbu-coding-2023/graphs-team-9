package viewModel

import androidx.compose.ui.graphics.Color
import graph.Edge
import kotlin.math.absoluteValue

class UndirectedEdgeVM(
    source: VertexVM,
    target: VertexVM,
    edge: Edge,
    basicColor: Color = Color.Black
) : EdgeVM(source, target, edge, basicColor) {
    override fun shouldColorEdge(
        sourcePathPosition: Int,
        targetPathPosition: Int,
    ): Boolean {
        return (sourcePathPosition - targetPathPosition).absoluteValue == 1
    }
}
