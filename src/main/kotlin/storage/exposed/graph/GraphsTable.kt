package storage.exposed.graph

import org.jetbrains.exposed.dao.id.IntIdTable

object GraphsTable : IntIdTable("Graphs") {
    val name = varchar("Name", 100)
    val type = varchar("Type", 20)
}
