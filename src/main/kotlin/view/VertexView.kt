import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import viewModel.VertexVM

@Composable
fun vertexView(viewModel: VertexVM) {
    val text = viewModel.data
    Box(
        modifier =
            Modifier
                .size(viewModel.size * 2)
                .offset(viewModel.x, viewModel.y)
                .background(viewModel.color, shape = CircleShape)
                .border(
                    width = viewModel.size / 10,
                    color = Color.Black,
                    shape = CircleShape,
                )
                .pointerInput(viewModel) {
                    detectTapGestures(
                        onTap = { viewModel.onTap() },
                    )
                }
                .pointerInput(viewModel) {
                    detectDragGestures { change, dragAmount ->
                        change.consume()
                        viewModel.onDrag(dragAmount)
                    }
                },
    ) {
        if (viewModel.insideDataVisibility) {
            Text(
                text = text,
                modifier =
                    Modifier
                        .align(Alignment.Center),
            )
        }
    }
    if (viewModel.outsideDataVisibility) {
        Text(
            modifier =
                Modifier
                    .offset(viewModel.x, viewModel.y - viewModel.size + 5.dp),
            text = text,
        )
    }
}
