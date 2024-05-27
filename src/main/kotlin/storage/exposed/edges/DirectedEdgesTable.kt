package storage.exposed.edges

import org.jetbrains.exposed.dao.id.IntIdTable
import storage.exposed.vertices.VerticesTable

object DirectedEdgesTable : IntIdTable("directedEdges") {
    val firstVertexValue = reference("firstVertexValue", VerticesTable)
    val secondVertexValue = reference("secondVertexValue", VerticesTable)
    val label = varchar("label", 100).nullable()
}