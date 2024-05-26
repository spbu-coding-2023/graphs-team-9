package view

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import vertexView
import viewModel.DirectedGraphVM

@Composable
fun directedGraphView(
    viewModel: DirectedGraphVM
) {
    Box(Modifier.fillMaxSize()) {
        viewModel.vertices.forEach { vertexView(it)}
        viewModel.edges.forEach{ directedEdgeView(it)}
    }
}
