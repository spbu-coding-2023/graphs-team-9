package storage.exposed.edges

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import storage.exposed.vertices.VertexEntity

class EdgeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EdgeEntity>(EdgesTable)

    var fromVertexValue by VertexEntity referencedOn EdgesTable.fromVertexValue
    var toVertexValue by VertexEntity referencedOn EdgesTable.toVertexValue
    var label by EdgesTable.label
    var weight by EdgesTable.weight
    var relatedGraphID by EdgesTable.relatedGraphID
}
