package storage.exposed

import graph.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import storage.exposed.edges.EdgesEntity
import storage.exposed.edges.EdgesTable
import storage.exposed.vertices.VerticesEntity
import storage.exposed.vertices.VerticesTable

const val dbPath = "./data/graphs-team-9.db"

class ExposedManager : ExposedManagerInterface {
    private val db by lazy {
        Database.connect("jdbc:sqlite:$dbPath", "org.sqlite.JDBC")
    }

    private fun initConnection() {
        transaction(db) {
            SchemaUtils.create(VerticesTable)
            SchemaUtils.create(EdgesTable)
        }
    }

    private fun clearTables() {
        initConnection()
        transaction {
            if (VerticesTable.selectAll().empty().not() || EdgesTable.selectAll().empty().not())
                {
                    VerticesTable.deleteAll()
                    EdgesTable.deleteAll()
                }
        }
    }

    override fun saveGraph(graph: Graph<String>) {
        initConnection()
        val vertexCount = graph.verticesCount()
        when (graph is UndirectedGraph<String>) {
            true -> {
                val graphsRepresentation = graph.svsEdgesList()
                for (edge in graphsRepresentation) {
                    transaction(db) {
                        EdgesEntity.new {
                            fromVertexValue =
                                VerticesEntity.new {
                                    value = edge.source().toString()
                                }
                            toVertexValue =
                                VerticesEntity.new {
                                    value = edge.target().toString()
                                }
                            label =
                                if (edge.label() == "") {
                                    null
                                } else {
                                    edge.label()
                                }
                        }
                    }
                }
            }

            else -> {
                val graphsRepresentation = graph.adjacencyList()
                for (sourceVertexIndex in 0 until vertexCount) {
                    for (outgoingEdge in 0 until graphsRepresentation.outgoingEdgesCount(sourceVertexIndex)) {
                        val edge = graphsRepresentation.getEdge(sourceVertexIndex, outgoingEdge)
                        transaction(db) {
                            EdgesEntity.new {
                                fromVertexValue =
                                    VerticesEntity.new {
                                        value = sourceVertexIndex.toString()
                                    }
                                toVertexValue =
                                    VerticesEntity.new {
                                        value = edge.target().toString()
                                    }
                                label =
                                    if (edge.label() == "") {
                                        null
                                    } else {
                                        edge.label()
                                    }
                            }
                        }
                    }
                }
            }
        }
    }

    override fun readGraph(
        fileName: String,
        db: Database,
    ): Graph<String> {
        TODO("Not yet implemented")
    }

//    override fun readGraph(fileName: String, db: Database): Graph<String> {
//        val db by lazy {
//            Database.connect("jdbc:sqlite:${fileName}", "org.sqlite.JDBC")
//        }
//        transaction(db) {
//            val query = EdgesTable.selectAll()
//            query.forEach {
//
//            }
//            val vertices = VerticesEntity.find { Vertices.graph eq graph.id }.map { it.name }
//            val edges = EdgesEntity.find { Edges.graph eq graph.id }.map { it.from.name to it.to.name }
//            vertices to edges
//        }
//    }
}
