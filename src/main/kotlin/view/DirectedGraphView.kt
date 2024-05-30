package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import vertexView
import viewModel.DirectedGraphVM

@Composable
fun directedGraphView(viewModel: DirectedGraphVM) {
    Box(
        Modifier.fillMaxSize().onGloballyPositioned {
                layoutCoordinates ->
            viewModel.height = layoutCoordinates.size.height
            viewModel.width = layoutCoordinates.size.width
        },
    ) {
        viewModel.vertices.forEach { vertexView(it) }
        viewModel.edges.forEach { directedEdgeView(it) }
    }
}
