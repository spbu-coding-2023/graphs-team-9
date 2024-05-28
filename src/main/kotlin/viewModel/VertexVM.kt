package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

class VertexVM(
    val data: String,
    x: Dp = 0.dp,
    y: Dp = 0.dp,
    size: Dp = 25.dp,
    color: Color = Color.White,
) {
    private var xStrategyBasedS = mutableStateOf(x)
    private var yStrategyBasedS = mutableStateOf(y)
    private var sizeS = mutableStateOf(size)
    private var colorS = mutableStateOf(color)
    private var pathPositionsS = mutableStateOf(arrayListOf<Int>())
    private var insideDataVisibilityS = mutableStateOf(false)
    private var outsideDataVisibilityS = mutableStateOf(false)
    private var xOffsetS = mutableStateOf(0.dp)
    private var yOffsetS = mutableStateOf(0.dp)

    var x: Dp
        get() = xStrategyBasedS.value + xOffsetS.value
        set(coordinate) {
            xStrategyBasedS.value = coordinate
        }

    var y: Dp
        get() = yStrategyBasedS.value + yOffsetS.value
        set(coordinate) {
            yStrategyBasedS.value = coordinate
        }

    var size: Dp
        get() = sizeS.value
        set(size) {
            sizeS.value = size
        }

    var color: Color
        get() = colorS.value
        set(color) {
            colorS.value = color
        }

    var pathPositions: ArrayList<Int>
        get() = pathPositionsS.value
        set(position) {
            pathPositionsS.value = position
        }

    var insideDataVisibility: Boolean
        get() = insideDataVisibilityS.value
        set(visibility) {
            insideDataVisibilityS.value = visibility
        }

    val outsideDataVisibility: Boolean
        get() = outsideDataVisibilityS.value

    fun onTap() {
        if (!insideDataVisibility) {
            outsideDataVisibilityS.value = !outsideDataVisibilityS.value
        }
    }

    fun onDrag(offset: Offset) {
        xOffsetS.value += offset.x.dp
        yOffsetS.value += offset.y.dp
    }

    fun removeOffset() {
        xOffsetS.value = 0.dp
        yOffsetS.value = 0.dp
    }
}
