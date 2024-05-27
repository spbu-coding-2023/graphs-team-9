package storage.exposed.edges

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import storage.exposed.vertices.VerticesEntity

class EdgesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EdgesEntity>(EdgesTable)

    var fromVertexValue by VerticesEntity referencedOn EdgesTable.fromVertexValue
    var toVertexValue by VerticesEntity referencedOn EdgesTable.toVertexValue
    var label by EdgesTable.label
}
