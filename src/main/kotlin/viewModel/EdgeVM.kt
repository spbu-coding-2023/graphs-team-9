package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graph.Edge

open class EdgeVM(
    val source: VertexVM,
    val target: VertexVM,
    val edge: Edge,
) {
    val isLoop: Boolean = (source === target)
    private var weightVisibilityS = mutableStateOf(false)
    private var labelVisibilityS = mutableStateOf(false)
    private var colorS = mutableStateOf(Color.Black)

    var weightVisibility: Boolean
        get() = weightVisibilityS.value
        set(visibility) {
            weightVisibilityS.value = visibility
        }

    var labelVisibility: Boolean
        get() = labelVisibilityS.value
        set(visibility) {
            labelVisibilityS.value = visibility
        }

    var color: Color
        get() = colorS.value
        set(color) {
            colorS.value = color
        }

    private fun abs(dp: Dp): Dp {
        if (dp > 0.dp) {
            return dp
        }
        return -dp
    }

    private enum class Side { Left, Right, Top, Bottom }

    private fun makeCoordinatesBySide(
        sourceSide: Side,
        targetSide: Side,
    ): Pair<Pair<Dp, Dp>, Pair<Dp, Dp>> {
        val sourceCoordinates: Pair<Dp, Dp> =
            when (sourceSide) {
                Side.Top -> Pair(source.x, source.y - source.size * 0.9f)
                Side.Right -> Pair(source.x + source.size * 0.9f, source.y)
                Side.Bottom -> Pair(source.x, source.y + source.size * 0.9f)
                Side.Left -> Pair(source.x - source.size * 0.9f, source.y)
            }
        val targetCoordinates: Pair<Dp, Dp> =
            when (targetSide) {
                Side.Top -> Pair(target.x, target.y - target.size * 0.9f)
                Side.Right -> Pair(target.x + target.size * 0.9f, target.y)
                Side.Bottom -> Pair(target.x, target.y + target.size * 0.9f)
                Side.Left -> Pair(target.x - target.size * 0.9f, target.y)
            }
        return Pair(sourceCoordinates, targetCoordinates)
    }

    fun computeCoordinates(): Pair<Pair<Dp, Dp>, Pair<Dp, Dp>> {
        if (!isLoop) {
            val horizontalRelativePosition = target.x - source.x
            val verticalRelativePosition = target.y - source.y
            return if (abs(horizontalRelativePosition) >= abs(verticalRelativePosition)) {
                if (horizontalRelativePosition >= 0.dp) {
                    makeCoordinatesBySide(Side.Right, Side.Left)
                } else {
                    makeCoordinatesBySide(Side.Left, Side.Right)
                }
            } else {
                if (verticalRelativePosition >= 0.dp) {
                    makeCoordinatesBySide(Side.Bottom, Side.Top)
                } else {
                    makeCoordinatesBySide(Side.Top, Side.Bottom)
                }
            }
        }
        return Pair(
            Pair(target.x - target.size * 1.2f, target.y - target.size * 1.2f),
            Pair(0.dp, 0.dp),
        )
    }
}
