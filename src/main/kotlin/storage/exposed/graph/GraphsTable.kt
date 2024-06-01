package storage.exposed.graph

import org.jetbrains.exposed.dao.id.IntIdTable

object GraphsTable : IntIdTable("Graphs") {
    val name = varchar("graphName", 100)
    val type = varchar("type", 20)
}
