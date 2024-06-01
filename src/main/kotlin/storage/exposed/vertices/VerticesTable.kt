package storage.exposed.vertices

import org.jetbrains.exposed.dao.id.IntIdTable

object VerticesTable : IntIdTable("Vertices") {
    val index = varchar("value", 50)
    val relatedGraphID = integer("relatedGraph")
}
