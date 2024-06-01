package storage.exposed.vertices

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VertexEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VertexEntity>(VerticesTable)
    var value by VerticesTable.index
}
