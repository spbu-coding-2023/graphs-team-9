package storage.exposed

import graph.*
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.transactions.transaction
import storage.exposed.edges.EdgeEntity
import storage.exposed.edges.EdgesTable
import storage.exposed.graph.GraphEntity
import storage.exposed.graph.GraphsTable
import storage.exposed.vertices.VertexEntity
import storage.exposed.vertices.VerticesTable

class SqlRepo {
    private val dbPath = "graph.db"

    private fun initConnection() {
        Database.connect("jdbc:sqlite:$dbPath", "org.sqlite.JDBC")
        transaction {
            addLogger(StdOutSqlLogger)
            SchemaUtils.create(GraphsTable, VerticesTable, EdgesTable)
        }
    }

    fun saveGraph(graphName: String, graph: Graph) {
        initConnection()
        val vertexCount = graph.verticesCount()
        if (graph is UndirectedGraph) {
            val graphsRepresentation = graph.svsEdgesList()
            initConnection()
            transaction {
                val graphID = GraphsTable.insertAndGetId {
                    it[name] = graphName
                    it[type] = "Undirected"
                }.value
                for (edge in graphsRepresentation) {
                    addLogger(StdOutSqlLogger)
                    EdgeEntity.new {
                        fromVertexValue =
                            VertexEntity.new {
                                value = graph.vertexValue(edge.source())
                            }
                        toVertexValue =
                            VertexEntity.new {
                                value = graph.vertexValue(edge.target())
                            }
                        label =
                            if (edge.label() == "") {
                                null
                            } else {
                                edge.label()
                            }
                        weight = edge.weight()
                        relatedGraphID = graphID
                    }
                }
            }
        }
        else {
            val graphsRepresentation = graph.adjacencyList()
            initConnection()
            transaction {
                val graphID = GraphsTable.insertAndGetId {
                    it[name] = graphName
                    it[type] = "Directed"
                }.value
                for (sourceVertexIndex in 0 until vertexCount) {
                    for (outgoingEdge in 0 until graphsRepresentation.outgoingEdgesCount(sourceVertexIndex)) {
                        val edge = graphsRepresentation.getEdge(sourceVertexIndex, outgoingEdge)
                        addLogger(StdOutSqlLogger)

                        EdgeEntity.new {
                            fromVertexValue =
                                VertexEntity.new {
                                    value = graph.vertexValue(sourceVertexIndex)
                                }
                            toVertexValue =
                                VertexEntity.new {
                                    value = graph.vertexValue(edge.target())
                                }
                            label =
                                if (edge.label() == "") {
                                    null
                                } else {
                                    edge.label()
                                }
                            weight = edge.weight()
                            relatedGraphID = graphID
                        }
                    }
                }
            }
        }
    }

    private fun getEmptyDirectedOnUndirectedGraph(type: String) : Graph {
        return if (type == "Undirected") UndirectedGraph()
        else DirectedGraph()
    }

    fun readGraph(
        graphName: String
    ) : Graph? {
        initConnection()
        transaction {
            var graphId = 0
            var graphType = ""
            GraphEntity.find { GraphsTable.name eq graphName }.firstOrNull()?.let {
                graphId = it.id.value
                graphType = it.type
            }
            val graph = getEmptyDirectedOnUndirectedGraph(graphType)
            VertexEntity.find { VerticesTable.relatedGraphID eq graphId }.forEach { vertex ->
                graph.addVertex(vertex.value)
            }
            EdgeEntity.find { EdgesTable.relatedGraphID eq graphId }.firstOrNull()?.let {  edge ->
                if (edge.label == null) {
                    graph.addEdge(edge.fromVertexValue.value, edge.toVertexValue.value, " ", edge.weight)
                }
                graph.addEdge(edge.fromVertexValue.value, edge.toVertexValue.value, edge.label.toString(), edge.weight)
            }
            return@transaction graph
        }
        return null
    }
}
