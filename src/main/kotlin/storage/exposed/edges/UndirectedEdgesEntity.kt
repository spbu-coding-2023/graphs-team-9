package storage.exposed.edges

import org.jetbrains.exposed.dao.IntEntity
import org.jetbrains.exposed.dao.IntEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class UndirectedEdgesEntity (id: EntityID<Int>) : IntEntity(id) {
    companion object : IntEntityClass<DirectedEdgesEntity>(DirectedEdgesTable)

    var firstVertexValue by UndirectedEdgesEntity referencedOn UndirectedEdgesTable.fromVertexValue
    var secondVertexValue by UndirectedEdgesEntity referencedOn UndirectedEdgesTable.toVertexValue
    var label by DirectedEdgesTable.label
}
