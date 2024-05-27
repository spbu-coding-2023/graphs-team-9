package storage.exposed.edges

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class DirectedEdgesEntity(id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DirectedEdgesEntity>(DirectedEdgesTable)

    var firstVertexValue by DirectedEdgesEntity referencedOn DirectedEdgesTable.firstVertexValue
    var secondVertexValue by DirectedEdgesEntity referencedOn DirectedEdgesTable.secondVertexValue
    var label by DirectedEdgesTable.label
}