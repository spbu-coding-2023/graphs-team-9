import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import view.menu
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

fun main() =
    application {
        if (!Path("graphs").exists()) Path("graphs").createDirectory()
        Window(
            onCloseRequest = ::exitApplication,
            title = "Graph Visualizer",
            state =
                rememberWindowState(
                    position = WindowPosition(alignment = Alignment.Center),
                ),
        ) {
            menu()
        }
    }
