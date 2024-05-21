package view

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
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
import kotlin.math.atan
import viewModel.EdgeVM
@Composable
fun directedEdgeView(viewModel: EdgeVM) {
    Canvas(
        modifier = Modifier.fillMaxSize(),
    ) {
        val coordinates = viewModel.computeCoordinates()
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
                Offset(x, y)
            ) {
                drawPath(
                    arrowPath,
                    Color.Black
                )
                println(
                    atan(
                        (endCoordinates.second.toPx() - startCoordinates.second.toPx())
                                / (endCoordinates.first.toPx() - startCoordinates.first.toPx())
                    )
                )
                println(
                    atan(
                        ((endCoordinates.first.toPx() - startCoordinates.first.toPx())
                                / (endCoordinates.second.toPx() - startCoordinates.second.toPx()))
                    )
                )
            }
            drawLine(
                start = Offset(
                    startCoordinates.first.toPx(),
                    startCoordinates.second.toPx(),
                ),
                end = Offset(
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