package storage.exposed.edges

import org.jetbrains.exposed.dao.id.IntIdTable
import storage.exposed.graph.GraphsTable
import storage.exposed.vertices.VerticesTable

object EdgesTable : IntIdTable("Edges") {
    val fromVertexIndex = reference("fromVertexIndex", VerticesTable)
    val toVertexIndex = reference("toVertexIndex", VerticesTable)
    val label = varchar("label", 100).nullable()
    val weight = double("weight")
    val relatedGraphID = integer("relatedGraph")
}
