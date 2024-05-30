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
    private var xS = mutableStateOf(x)
    private var yS = mutableStateOf(y)
    private var sizeS = mutableStateOf(size)
    private var colorS = mutableStateOf(color)
    private var pathPositionsS = mutableStateOf(arrayListOf<Int>())
    private var insideDataVisibilityS = mutableStateOf(false)
    private var outsideDataVisibilityS = mutableStateOf(false)

    private val defaultX = x
    private val defaultY = y
    var x: Dp
        get() = xS.value
        set(coordinate) {
            xS.value = coordinate
        }

    var y: Dp
        get() = yS.value
        set(coordinate) {
            yS.value = coordinate
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
        xS.value += offset.x.dp
        yS.value += offset.y.dp
    }

    fun removeOffset() {
        xS.value = defaultX
        yS.value = defaultY
    }
}
