package storage.exposed

import graph.Graph
import org.jetbrains.exposed.sql.Database

interface ExposedManagerInterface {
    fun saveGraph(graph: Graph<String>)

    fun readGraph(
        fileName: String,
        db: Database,
    ): Graph<String>
}
