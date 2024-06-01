package storage.exposed.vertices

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VerticeEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VerticeEntity>(VerticesTable)
    var value by VerticesTable.index
}
