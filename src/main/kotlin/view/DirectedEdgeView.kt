package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.drawscope.rotateRad
import androidx.compose.ui.unit.dp
import viewModel.DirectedEdgeVM
import kotlin.math.atan

@Composable
fun directedEdgeView(viewModel: DirectedEdgeVM) {
    val coordinates = viewModel.computeCoordinates()
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        if (!viewModel.isLoop) {
            val startCoordinates = coordinates.first
            val endCoordinates = coordinates.second
            val deltaX = endCoordinates.first.toPx() - startCoordinates.first.toPx()
            val triangleHalfBase = 6.dp.toPx()
            val triangleHeight = if (deltaX > 0f) 20.dp.toPx() else -20.dp.toPx()
            val x = endCoordinates.first.toPx()
            val y = endCoordinates.second.toPx()

            val arrowPath =
                Path().apply {
                    moveTo(x, y)
                    lineTo(x - triangleHeight, y - triangleHalfBase)
                    lineTo(x - triangleHeight, y + triangleHalfBase)
                }
            rotateRad(
                atan((endCoordinates.second.toPx() - startCoordinates.second.toPx()) / deltaX),
                Offset(x, y),
            ) {
                drawPath(
                    arrowPath,
                    viewModel.color,
                )
            }
            drawLine(
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
                color = viewModel.color,
                strokeWidth = 3f,
            )
        } else {
            val arcVertRadius = viewModel.target.size.toPx() * 0.6f
            rotate(30f, Offset(coordinates.first.first.toPx(), coordinates.first.second.toPx())) {
                drawArc(
                    color = viewModel.color,
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
    if (viewModel.labelVisibility) {
        val modifier =
            if (!viewModel.isLoop) {
                Modifier
                    .offset(
                        coordinates.first.first * 0.8f + coordinates.second.first * 0.2f,
                        coordinates.first.second * 0.8f + coordinates.second.second * 0.2f - 25.dp,
                    )
                    .background(Color.White)
            } else {
                Modifier
                    .offset(coordinates.first.first - 40.dp, coordinates.first.second - 10.dp)
                    .background(Color.White)
            }
        Text(
            modifier = modifier,
            text = viewModel.edge.label(),
        )
    }
    if (viewModel.weightVisibility) {
        val modifier =
            if (!viewModel.isLoop) {
                Modifier
                    .offset(
                        coordinates.first.first * 0.8f + coordinates.second.first * 0.2f,
                        coordinates.first.second * 0.8f + coordinates.second.second * 0.2f + 15.dp,
                    )
                    .background(Color.White)
            } else {
                Modifier
                    .offset(coordinates.first.first, coordinates.first.second - 15.dp)
                    .background(Color.White)
            }
        Text(
            modifier = modifier,
            text = viewModel.edge.weight().toString(),
        )
    }
}
