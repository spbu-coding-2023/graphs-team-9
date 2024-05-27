package graph.undirectedgraph

import graph.*
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class UndirectedGraphTest {
    private lateinit var graph: UndirectedGraph
    private lateinit var vertexValues: ArrayList<String>
    private lateinit var svsEdgesList: ArrayList<SourceVertexStoringEdge>

    @BeforeEach
    fun setup() {
        svsEdgesList = arrayListOf()
        vertexValues = arrayListOf()
    }

    @Nested
    inner class SecondaryConstructorTests {

        @Test
        fun `source doesn't exist`() {
            svsEdgesList.add(SourceVertexStoringEdge(1, 0, "", 1.0))
            vertexValues.add("0")
            assertFailsWith<IllegalArgumentException> { graph = UndirectedGraph(svsEdgesList, vertexValues) }
        }

        @Test
        fun `target doesn't exist`() {
            svsEdgesList.add(SourceVertexStoringEdge(0, 1, "", 1.0))
            vertexValues.add("0")
            assertFailsWith<IllegalArgumentException> { graph = UndirectedGraph(svsEdgesList, vertexValues) }
        }
    }

    @Nested
    inner class SVSEdgesListTests {

        @Test
        fun `svsEdgesList is empty`() {
            assertEquals(svsEdgesList, UndirectedGraph(svsEdgesList, vertexValues).svsEdgesList())
        }

        @Test
        fun `svsEdgesList is not empty`() {
            svsEdgesList.add(SourceVertexStoringEdge(0, 1))
            vertexValues.add("0")
            vertexValues.add("1")
            assertEquals(svsEdgesList, UndirectedGraph(svsEdgesList, vertexValues).svsEdgesList())
        }
    }

    @Nested
    inner class AdjacencyListTests {

        @Test
        fun `empty list`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            val adjacencyList = graph.adjacencyList()
            assertEquals(0, adjacencyList.verticesCount())
        }

        @Test
        fun `list with one edge`() {
            val edge = SourceVertexStoringEdge(0, 1, "label", 2.0)
            svsEdgesList.add(edge)
            vertexValues.add("0")
            vertexValues.add("1")
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            val adjacencyList = graph.adjacencyList()
            assertEquals(2, adjacencyList.verticesCount())
            assertEquals(1, adjacencyList.outgoingEdgesCount(0))
            assertEquals(1, adjacencyList.outgoingEdgesCount(1))

            assertEquals(edge.target(), adjacencyList.getEdge(0, 0).target())
            assertEquals(edge.label(), adjacencyList.getEdge(0, 0).label())
            assertEquals(edge.weight(), adjacencyList.getEdge(0, 0).weight())

            assertEquals(edge.source(), adjacencyList.getEdge(1, 0).target())
            assertEquals(edge.label(), adjacencyList.getEdge(1, 0).label())
            assertEquals(edge.weight(), adjacencyList.getEdge(1, 0).weight())
        }
//
//        @Test
//        fun `list with more than one vertices`() {
//            adjacencyList = UndirectedAdjacencyList(3)
//            vertexValues = arrayListOf("0", "1", "2")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertEquals(adjacencyList, graph.adjacencyList())
//        }
//
//        @Test
//        fun `list with one edge`() {
//            adjacencyList = DirectedAdjacencyList(2)
//            adjacencyList.addEdge(0, 1, "", 1.0)
//            vertexValues = arrayListOf("0", "1")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertEquals(adjacencyList, graph.adjacencyList())
//        }
//
//        @Test
//        fun `list with more than one edges`() {
//            adjacencyList = DirectedAdjacencyList(3)
//            vertexValues = arrayListOf("0", "1", "2")
//            adjacencyList.addEdge(0, 1, "label", 1.0)
//            adjacencyList.addEdge(1, 0, "laba", 3.0)
//            adjacencyList.addEdge(0, 2, "", 2.0)
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertEquals(adjacencyList, graph.adjacencyList())
//        }
    }
//
//    @Nested
//    inner class SVSEdgesListTests {
//        private lateinit var expectedResult: List<SourceVertexStoringEdge>
//        private lateinit var result: List<SourceVertexStoringEdge>
//
//        @Test
//        fun `empty svs list`() {
//            adjacencyList = DirectedAdjacencyList(1)
//            vertexValues = arrayListOf("0")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            expectedResult = arrayListOf()
//            result = graph.svsEdgesList()
//            assertEquals(expectedResult, result)
//        }
//
//        @Test
//        fun `svs list with more than zero edges`() {
//            adjacencyList = DirectedAdjacencyList(2)
//            vertexValues = arrayListOf("0", "1")
//            adjacencyList.addEdge(0, 1, "", 1.0)
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            expectedResult = arrayListOf(SourceVertexStoringEdge(0, 1, "", 1.0))
//            result = graph.svsEdgesList()
//
//            assertEquals(expectedResult.size, result.size)
//            assertEquals(expectedResult[0].source(), result[0].source())
//            assertEquals(expectedResult[0].target(), result[0].target())
//            assertEquals(expectedResult[0].label(), result[0].label())
//            assertEquals(expectedResult[0].weight(), result[0].weight())
//        }
//
//        @Test
//        fun `svs list with several edges from one vertex`() {
//            adjacencyList = DirectedAdjacencyList(3)
//            vertexValues = arrayListOf("0", "1", "2")
//            adjacencyList.addEdge(0, 1, "", 1.0)
//            adjacencyList.addEdge(0, 2, "label", 2.0)
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            expectedResult = arrayListOf(
//                SourceVertexStoringEdge(0, 1, "", 1.0),
//                SourceVertexStoringEdge(0, 2, "label", 2.0)
//            )
//            result = graph.svsEdgesList()
//
//            assertEquals(expectedResult.size, result.size)
//
//            assertEquals(expectedResult[0].source(), result[0].source())
//            assertEquals(expectedResult[0].target(), result[0].target())
//            assertEquals(expectedResult[0].label(), result[0].label())
//            assertEquals(expectedResult[0].weight(), result[0].weight())
//
//            assertEquals(expectedResult[1].source(), result[1].source())
//            assertEquals(expectedResult[1].target(), result[1].target())
//            assertEquals(expectedResult[1].label(), result[1].label())
//            assertEquals(expectedResult[1].weight(), result[1].weight())
//        }
//
//        @Test
//        fun `svs list where inverted edges are two different edges`() {
//            adjacencyList = DirectedAdjacencyList(2)
//            vertexValues = arrayListOf("0", "1")
//            adjacencyList.addEdge(0, 1, "", 1.0)
//            adjacencyList.addEdge(1, 0, "label", 2.0)
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            expectedResult = arrayListOf(
//                SourceVertexStoringEdge(0, 1, "", 1.0),
//                SourceVertexStoringEdge(1, 0, "label", 2.0)
//            )
//            result = graph.svsEdgesList()
//
//            assertEquals(expectedResult.size, result.size)
//
//            assertEquals(expectedResult[0].source(), result[0].source())
//            assertEquals(expectedResult[0].target(), result[0].target())
//            assertEquals(expectedResult[0].label(), result[0].label())
//            assertEquals(expectedResult[0].weight(), result[0].weight())
//
//            assertEquals(expectedResult[1].source(), result[1].source())
//            assertEquals(expectedResult[1].target(), result[1].target())
//            assertEquals(expectedResult[1].label(), result[1].label())
//            assertEquals(expectedResult[1].weight(), result[1].weight())
//        }
//    }
//
//    @Nested
//    inner class VerticesCountTests {
//
//        @Test
//        fun `zero vertices`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertEquals(0, graph.verticesCount())
//        }
//
//        @Test
//        fun `more than zero vertices`() {
//            adjacencyList = DirectedAdjacencyList(1)
//            vertexValues = arrayListOf("0")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertEquals(1, graph.verticesCount())
//        }
//    }
//
//    @Nested
//    inner class IsWeightedTests {
//
//        @Test
//        fun `not weighted`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            assertFalse(graph.isWeighted())
//        }
//
//        @Test
//        fun weighted() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            graph.weighted = true
//            assertTrue(graph.isWeighted())
//        }
//    }
//
//    @Nested
//    inner class AddVertexTests {
//
//        @Test
//        fun `graph is immutable`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            graph.makeItLighterAndImmutable()
//            assertFailsWith<IllegalArgumentException> { graph.addVertex("0") }
//        }
//
//        @Test
//        fun `graph is empty`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            graph.addVertex("0")
//            graph.makeItLighterAndImmutable()
//
//            assertEquals(1, graph.adjacencyList().verticesCount())
//            assertEquals(arrayListOf("0"), vertexValues)
//        }
//
//        @Test
//        fun `graph is not empty`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.makeItLighterAndImmutable()
//
//            assertEquals(2, graph.adjacencyList().verticesCount())
//            assertEquals(arrayListOf("0", "1"), vertexValues)
//        }
//
//        @Test
//        fun `duplicate vertex`() {
//            adjacencyList = DirectedAdjacencyList()
//            vertexValues = arrayListOf()
//            graph = DirectedGraph(adjacencyList, vertexValues)
//            graph.addVertex("0")
//            graph.addVertex("0")
//            graph.makeItLighterAndImmutable()
//
//            assertEquals(1, graph.adjacencyList().verticesCount())
//            assertEquals(arrayListOf(0), vertexValues)
//        }
//    }
//
//    @Nested
//    inner class VertexValueTest {
//
//        @Test
//        fun `index of vertex is zero`() {
//            adjacencyList = DirectedAdjacencyList(1)
//            vertexValues = arrayListOf("0")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//
//            assertEquals("0", graph.vertexValue(0))
//        }
//
//        @Test
//        fun `index of vertex is more than zero`() {
//            adjacencyList = DirectedAdjacencyList(2)
//            vertexValues = arrayListOf("0", "1")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//
//            assertEquals("1", graph.vertexValue(1))
//        }
//
//        @Test
//        fun `index of vertex doesn't exist`() {
//            adjacencyList = DirectedAdjacencyList(1)
//            vertexValues = arrayListOf("0")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//
//            assertFailsWith<IllegalArgumentException> { graph.vertexValue(1) }
//        }
//
//        @Test
//        fun `index of vertex is less than zero`() {
//            adjacencyList = DirectedAdjacencyList(1)
//            vertexValues = arrayListOf("0")
//            graph = DirectedGraph(adjacencyList, vertexValues)
//
//            assertFailsWith<IllegalArgumentException> { graph.vertexValue(- 1) }
//        }
//    }
//
//    @Nested
//    inner class AddEdgeTests {
//        private lateinit var expectedResult: Edge
//        private lateinit var graphAdList: DirectedAdjacencyList
//        private val graph = DirectedGraph()
//
//        @Test
//        fun `graph is immutable`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.makeItLighterAndImmutable()
//            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1") }
//        }
//
//        @Test
//        fun `source isn't in graph`() {
//            graph.addVertex("0")
//            assertFailsWith<IllegalArgumentException> { graph.addEdge("1", "0") }
//        }
//
//        @Test
//        fun `target isn't in graph`() {
//            graph.addVertex("0")
//            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1") }
//        }
//
//        @Test
//        fun `add edge in empty graph`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.addEdge("0", "1", "", -1.0)
//
//            expectedResult = Edge(1, "", -1.0)
//            graphAdList = graph.adjacencyList()
//
//            assertEquals(2, graphAdList.verticesCount())
//            assertEquals(expectedResult.target(), graphAdList.getEdge(0, 0).target())
//            assertEquals(expectedResult.label(), graphAdList.getEdge(0, 0).label())
//            assertEquals(expectedResult.weight(), graphAdList.getEdge(0, 0).weight())
//        }
//
//        @Test
//        fun `add edge in not empty graph`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.addVertex("2")
//            graph.addEdge("0", "1", "", -1.0)
//            graph.addEdge("1", "2", "label", 2.0)
//
//            expectedResult = Edge(2, "label", 2.0)
//            graphAdList = graph.adjacencyList()
//
//            assertEquals(3, graphAdList.verticesCount())
//            assertEquals(expectedResult.target(), graphAdList.getEdge(1, 0).target())
//            assertEquals(expectedResult.label(), graphAdList.getEdge(1, 0).label())
//            assertEquals(expectedResult.weight(), graphAdList.getEdge(1, 0).weight())
//        }
//
//        @Test
//        fun `add several edges to one vertex`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.addVertex("2")
//            graph.addEdge("0", "1", "", -1.0)
//            graph.addEdge("0", "2", "label", 1.0)
//
//            expectedResult = Edge(2, "label", 1.0)
//            graphAdList = graph.adjacencyList()
//
//            assertEquals(3, graph.adjacencyList().verticesCount())
//            assertEquals(expectedResult.target(), graphAdList.getEdge(0, 1).target())
//            assertEquals(expectedResult.label(), graphAdList.getEdge(0, 1).label())
//            assertEquals(expectedResult.weight(), graphAdList.getEdge(0, 1).weight())
//        }
//
//        @Test
//        fun `add inverted edge is not an error`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.addEdge("0", "1", "", -1.0)
//            graph.addEdge("1", "0", "label", 1.0)
//
//            expectedResult = Edge(0, "label", 1.0)
//            graphAdList = graph.adjacencyList()
//
//            assertEquals(2, graph.adjacencyList().verticesCount())
//            assertEquals(expectedResult.target(), graphAdList.getEdge(1, 0).target())
//            assertEquals(expectedResult.label(), graphAdList.getEdge(1, 0).label())
//            assertEquals(expectedResult.weight(), graphAdList.getEdge(1, 0).weight())
//        }
//
//        @Test
//        fun `duplicate edge`() {
//            graph.addVertex("0")
//            graph.addVertex("1")
//            graph.addEdge("0", "1", "", -1.0)
//            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1", "label", 1.0) }
//        }
//    }
}
