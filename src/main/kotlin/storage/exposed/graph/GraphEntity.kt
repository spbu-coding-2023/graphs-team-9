package storage.exposed.graph

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import storage.exposed.edges.EdgesTable

class GraphEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<GraphEntity>(GraphsTable)
    var name by GraphsTable.name
    var type by GraphsTable.type
    var relatedGraphID by EdgesTable.relatedGraphID
}