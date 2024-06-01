package storage.exposed.edges

import org.jetbrains.exposed.dao.id.IntIdTable
import storage.exposed.graph.GraphsTable
import storage.exposed.vertices.VerticesTable

object EdgesTable : IntIdTable("Edges") {
    val fromVertexValue = reference("FromVertexValue", VerticesTable)
    val toVertexValue = reference("ToVertexValue", VerticesTable)
    val label = varchar("Label", 100).nullable()
    val weight = double("Weight")
    val relatedGraphID = integer("RelatedGraph")
}
