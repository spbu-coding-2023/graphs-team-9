// package storage.exposed.graphs
//
// import org.jetbrains.exposed.dao.IntEntityClass
// import storage.exposed.edges.EdgesEntity
// import storage.exposed.edges.EdgesTable
// import storage.exposed.graphs.GraphsTable.reference
// import storage.exposed.vertices.VerticesEntity
//
// class GraphsEntity {
//    val edges = reference("toVertexValue", GraphsTable)
//    companion object : IntEntityClass<EdgesEntity>(EdgesTable)
//
//    var fromVertexValue by VerticesEntity referencedOn EdgesTable.fromVertexValue
//
// }
