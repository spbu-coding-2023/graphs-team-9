package storage.exposed.edges

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import storage.exposed.vertices.VerticesEntity

class EdgesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<EdgesEntity>(EdgesTable)

    var fromVertexIndex by VerticesEntity referencedOn EdgesTable.fromVertexIndex
    var toVertexIndex by VerticesEntity referencedOn EdgesTable.toVertexIndex
    var weight by EdgesTable.weight
    var label by EdgesTable.label
    var type by EdgesTable.type
}
