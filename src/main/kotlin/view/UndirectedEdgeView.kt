package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.unit.dp
import viewModel.EdgeVM

@Composable
fun undirectedEdgeView(viewModel: EdgeVM) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val coordinates = viewModel.computeCoordinates()

        if (!viewModel.isLoop) {
            val startCoordinates = coordinates.first
            val endCoordinates = coordinates.second
            drawLine(
                color = Color.Black,
                start =
                    Offset(
                        startCoordinates.first.toPx(),
                        startCoordinates.second.toPx(),
                    ),
                end =
                    Offset(
                        endCoordinates.first.toPx(),
                        endCoordinates.second.toPx(),
                    ),
                strokeWidth = 3.dp.toPx(),
            )
        } else {
            val arcVertRadius = viewModel.target.size.toPx() * 0.6f
            rotate(30f, Offset(coordinates.first.first.toPx(), coordinates.first.second.toPx())) {
                drawArc(
                    color = Color.Black,
                    startAngle = 70f,
                    sweepAngle = 230f,
                    useCenter = false,
                    size = Size(arcVertRadius * 2, arcVertRadius),
                    topLeft =
                        Offset(
                            coordinates.first.first.toPx(),
                            coordinates.first.second.toPx(),
                        ),
                    style = Stroke(width = 3.dp.toPx()),
                )
            }
        }
    }
}
