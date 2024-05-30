import androidx.compose.ui.Alignment
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.WindowPosition
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import graph.Neo4jRepository
import view.menu
import kotlin.io.path.Path
import kotlin.io.path.createDirectory
import kotlin.io.path.exists

var neo4jRepository: Neo4jRepository? = null

fun main() =
    application {
        if (!Path("graphs").exists()) Path("graphs").createDirectory()
        Window(
            onCloseRequest = {
                neo4jRepository?.close()
                exitApplication()
            },
            title = "Graph Visualizer",
            state =
                rememberWindowState(
                    position = WindowPosition(alignment = Alignment.Center),
                ),
        ) {
            menu()
        }
    }
