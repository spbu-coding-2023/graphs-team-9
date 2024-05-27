package storage.exposed.edges

import org.jetbrains.exposed.dao.id.IntIdTable
import storage.exposed.vertices.VerticesTable

object UndirectedEdgesTable : IntIdTable("undirectedEdges") {
    val fromVertexValue = reference("fromVertexValue", VerticesTable)
    val toVertexValue = reference("toVertexValue", VerticesTable)
    val label = varchar("label", 100).nullable()
}
