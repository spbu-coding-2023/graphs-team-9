package storage.exposed.edges

import org.jetbrains.exposed.dao.id.IntIdTable
import storage.exposed.vertices.VerticesTable

object EdgesTable : IntIdTable("directedEdges") {
    val fromVertexValue = reference("fromVertexValue", VerticesTable)
    val toVertexValue = reference("toVertexValue", VerticesTable)
    val label = varchar("label", 100).nullable()
}
