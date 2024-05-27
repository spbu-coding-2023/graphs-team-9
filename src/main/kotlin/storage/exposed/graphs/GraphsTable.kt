package storage.exposed.graphs

import org.jetbrains.exposed.dao.id.IntIdTable

object GraphsTable : IntIdTable("graphs") {
    val edges = reference("toVertexValue", GraphsTable)
//    val value = varchar("value", 50)
//    val x = integer("x")
//    val y = integer("y")
//    val colour
}
