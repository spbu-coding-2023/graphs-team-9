package storage.exposed.vertices

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class VerticesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<VerticesEntity>(VerticesTable)

    var value by VerticesTable.value
//    var x by VerticesTable.x
//    var y by VerticesTable.y
//    var color
}
