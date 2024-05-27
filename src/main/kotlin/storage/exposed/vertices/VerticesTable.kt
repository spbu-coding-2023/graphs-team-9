package storage.exposed.vertices

import org.jetbrains.exposed.dao.id.IntIdTable

object VerticesTable : IntIdTable("vertex") {
    val value = varchar("value", 50)
//    val x = integer("x")
//    val y = integer("y")
//    val colour
}
