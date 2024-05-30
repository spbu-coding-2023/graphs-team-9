package graph
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import org.neo4j.driver.AuthTokens
import org.neo4j.driver.GraphDatabase
import viewModel.DirectedGraphVM
import viewModel.GraphVM
import viewModel.UndirectedGraphVM
import java.io.Closeable

class Neo4jRepository(uri: String, user: String, password: String) : Closeable {
    private val driver = GraphDatabase.driver(uri, AuthTokens.basic(user, password))
    private val session = driver.session()

    fun putGraph(
        graph: Graph,
        graphName: String,
        isDirected: Boolean,
    ) {
        val commands = StringBuilder("CREATE (graph: Graph {name: '$graphName', isDirected: $isDirected})\n")
        for (v in 0 until graph.verticesCount()) {
            commands.append("CREATE (v$v: Vertex {data: '${graph.vertexValue(v)}'})\n")
            commands.append("CREATE (v$v)-[:PartOf]->(graph)\n")
        }
        if (isDirected) {
            for (v in 0 until graph.verticesCount()) {
                for (edgeOrdinalNumber in 0 until graph.adjacencyList().outgoingEdgesCount(v)) {
                    val edge = graph.adjacencyList().getEdge(v, edgeOrdinalNumber)
                    commands.append(
                        "CREATE (v$v)-[:Edge {label: '${edge.label()}', weight: ${edge.weight()}}]->(v${edge.target()})\n",
                    )
                }
            }
        } else {
            for (edge in graph.svsEdgesList()) {
                commands.append(
                    "CREATE (v${edge.source()})-[:Edge {label: '${edge.label()}', weight: ${edge.weight()}}]->(v${edge.target()})\n",
                )
            }
        }
        session.executeWrite { tc ->
            tc.run(commands.toString())
        }
    }

    fun putAnalyzedGraphVM(
        graphVM: GraphVM,
        graphName: String,
        isDirected: Boolean,
    ) {
        val width = graphVM.width
        val height = graphVM.height
        val standardWidth = graphVM.standardWidth
        val standardHeight = graphVM.standardHeight
        val commands = StringBuilder("CREATE (graph: GraphAnl {name : '$graphName', isDirected: $isDirected})\n")
        graphVM.vertices.forEach {
            val size = (it.size - 10.dp) / (height * width) * (standardHeight * standardWidth) + 10.dp
            commands.append(
                "CREATE (v${it.data}: VertexAnl {data: '${it.data}', size: ${size.value}, colorR: ${it.color.red}," +
                    "colorG: ${it.color.green}, colorB: ${it.color.blue}, x: ${it.x.value}, y: ${it.y.value}})\n",
            )
            commands.append("CREATE (v${it.data})-[:PartOf]->(graph)\n")
        }
        graphVM.edges.forEach {
            commands.append(
                "CREATE (v${it.source.data})-[:EdgeAnl {label: '${it.edge.label()}', weight: ${it.edge.weight()}," +
                    " colorR: ${it.color.red}, colorG: ${it.color.green}, colorB: ${it.color.blue} }]->(v${it.target.data})\n",
            )
        }
        session.executeWrite { tx -> tx.run(commands.toString()) }
    }

    private fun getGraphContent(
        emptyGraph: Graph,
        dbGraphName: String,
    ) {
        val graph = emptyGraph
        val commands =
            StringBuilder(
                "MATCH (g: Graph {name : '$dbGraphName'})\nMATCH (v: Vertex) - [:PartOf]->(g)",
            )
        commands.append("OPTIONAL MATCH (v)-[e: Edge]->(u: Vertex)\nRETURN v.data AS vertexValue1, ")
        commands.append("u.data AS vertexValue2, e.label AS edgeLabel, e.weight AS edgeWeight")
        session.executeRead { tx ->
            tx.run(commands.toString()).stream().forEach { rec ->
                val vertexValue1 = rec["vertexValue1"].asString()
                graph.addVertex(vertexValue1)
                val vertexValue2 = rec["vertexValue2"]
                if (!vertexValue2.isNull) {
                    graph.addVertex(vertexValue2.asString())
                    graph.addEdge(vertexValue1, vertexValue2.asString(), rec["edgeLabel"].asString(), rec["edgeWeight"].asDouble())
                }
            }
        }
    }

    fun getUndirectedGraph(graphName: String): UndirectedGraph {
        val graph = UndirectedGraph()
        getGraphContent(graph, graphName)
        return graph
    }

    fun getDirectedGraph(graphName: String): DirectedGraph {
        val graph = DirectedGraph()
        getGraphContent(graph, graphName)
        return graph
    }

    private fun getGraphVMContent(
        emptyGraph: Graph,
        dbGraphName: String,
        verticesColors: ArrayList<Color>,
        coordinates: ArrayList<Pair<Double, Double>>,
        sizes: ArrayList<Dp>,
        edgesColors: HashMap<Pair<String, String>, Color>,
    ) {
        val graph = emptyGraph
        session.executeRead { tx ->
            val commands =
                StringBuilder(
                    "MATCH (g: GraphAnl {name : '$dbGraphName'})\n MATCH (v: VertexAnl) - [:PartOf]->(g) ",
                )
            commands.append(
                "OPTIONAL MATCH (v)-[e: EdgeAnl]->(u: VertexAnl)\nRETURN v.data AS value1, v.size AS size1, ",
            )
            commands.append(
                "v.colorR AS red1, v.colorG AS green1,  v.colorB AS blue1,  v.x AS x1, v.y AS y1, u.data AS value2, ",
            )
            commands.append(
                "u.size AS size2, u.colorR AS red2, u.colorG AS green2, u.colorB AS blue2, u.x AS x2, u.y AS y2, ",
            )
            commands.append(
                "e.label AS edgeLabel, e.weight AS edgeWeight, e.colorR AS redE, e.colorG AS greenE, e.colorB AS blueE",
            )
            var verticesCount = 0
            tx.run(commands.toString()).stream().forEach { rec ->
                val vertexValue1 = rec["value1"].asString()
                graph.addVertex(vertexValue1)
                if (graph.verticesCount() > verticesCount) {
                    verticesCount++
                    verticesColors.add(
                        Color(rec["red1"].asDouble().toFloat(), rec["green1"].asDouble().toFloat(), rec["blue1"].asDouble().toFloat()),
                    )
                    sizes.add(rec["size1"].asDouble().dp)
                    coordinates.add(Pair(rec["x1"].asDouble(), rec["y1"].asDouble()))
                }
                val vertexValue2 = rec["value2"]
                if (!vertexValue2.isNull) {
                    graph.addVertex(vertexValue2.asString())
                    if (graph.verticesCount() > verticesCount) {
                        verticesCount++
                        verticesColors.add(
                            Color(rec["red2"].asDouble().toFloat(), rec["green2"].asDouble().toFloat(), rec["blue2"].asDouble().toFloat()),
                        )
                        sizes.add(rec["size2"].asDouble().dp)
                        coordinates.add(Pair(rec["x2"].asDouble(), rec["y2"].asDouble()))
                    }
                    graph.addEdge(
                        vertexValue1,
                        vertexValue2.asString(),
                        rec["edgeLabel"].asString(),
                        rec["edgeWeight"].asDouble(),
                    )
                    edgesColors[Pair(vertexValue1, vertexValue2.asString())] =
                        Color(rec["redE"].asDouble().toFloat(), rec["greenE"].asDouble().toFloat(), rec["blueE"].asDouble().toFloat())
                }
            }
        }
    }

    fun getUndirectedAnalyzedGraphVM(graphName: String): UndirectedGraphVM {
        val graph = UndirectedGraph()
        val verticesColors = arrayListOf<Color>()
        val coordinates = arrayListOf<Pair<Double, Double>>()
        val sizes = arrayListOf<Dp>()
        val edgesColors = hashMapOf<Pair<String, String>, Color>()
        getGraphVMContent(graph, graphName, verticesColors, coordinates, sizes, edgesColors)
        return UndirectedGraphVM(graph, verticesColors, coordinates, sizes, edgesColors)
    }

    fun getDirectedAnalyzedGraphVM(graphName: String): DirectedGraphVM {
        val graph = DirectedGraph()
        val verticesColors = arrayListOf<Color>()
        val coordinates = arrayListOf<Pair<Double, Double>>()
        val sizes = arrayListOf<Dp>()
        val edgesColors = hashMapOf<Pair<String, String>, Color>()
        getGraphVMContent(graph, graphName, verticesColors, coordinates, sizes, edgesColors)
        return DirectedGraphVM(graph, verticesColors, coordinates, sizes, edgesColors)
    }

    fun dbGraphsInfo(): ArrayList<GraphInfo> {
        val dbGraphsInfo = arrayListOf<GraphInfo>()
        session.executeRead { tx ->
            tx.run(
                "MATCH(g: Graph) RETURN g.name AS name, g.isDirected AS isDirected",
            ).stream().forEach { rec ->
                dbGraphsInfo.add(GraphInfo(rec["name"].asString(), rec["isDirected"].asBoolean(), false))
            }
        }
        session.executeRead { tx ->
            tx.run(
                "MATCH(g: GraphAnl) RETURN g.name AS name, g.isDirected AS isDirected",
            ).stream().forEach { rec ->
                dbGraphsInfo.add(GraphInfo(rec["name"].asString(), rec["isDirected"].asBoolean(), true))
            }
        }
        return dbGraphsInfo
    }

    override fun close() {
        session.close()
        driver.close()
    }
}
