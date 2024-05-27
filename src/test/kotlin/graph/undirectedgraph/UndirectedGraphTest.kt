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
        fun `source is less than zero`() {
            svsEdgesList.add(SourceVertexStoringEdge(-1, 0, "", 1.0))
            vertexValues.add("0")
            vertexValues.add("1")
            assertFailsWith<IllegalArgumentException> { graph = UndirectedGraph(svsEdgesList, vertexValues) }
        }

        @Test
        fun `target doesn't exist`() {
            svsEdgesList.add(SourceVertexStoringEdge(0, 1, "", 1.0))
            vertexValues.add("0")
            assertFailsWith<IllegalArgumentException> { graph = UndirectedGraph(svsEdgesList, vertexValues) }
        }

        @Test
        fun `target is less than zero`() {
            svsEdgesList.add(SourceVertexStoringEdge(0, -1, "", 1.0))
            vertexValues.add("0")
            vertexValues.add("1")
            assertFailsWith<IllegalArgumentException> { graph = UndirectedGraph(svsEdgesList, vertexValues) }
        }

        @Test
        fun `svsList is empty`() {
            assertNotNull(UndirectedGraph(svsEdgesList, vertexValues))
        }

        @Test
        fun `svsList is not empty`() {
            vertexValues.add("0")
            vertexValues.add("1")
            svsEdgesList.add(SourceVertexStoringEdge(0, 1))
            assertNotNull(UndirectedGraph(svsEdgesList, vertexValues))
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

        @Test
        fun `several edges from one vertex`() {
            val firstEdge = SourceVertexStoringEdge(0, 1, "label", 2.0)
            val secondEdge = SourceVertexStoringEdge(2, 0, "", 1.0)
            svsEdgesList.add(firstEdge)
            svsEdgesList.add(secondEdge)
            vertexValues.add("0")
            vertexValues.add("1")
            vertexValues.add("2")
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            val adjacencyList = graph.adjacencyList()
            assertEquals(3, adjacencyList.verticesCount())
            assertEquals(2, adjacencyList.outgoingEdgesCount(0))
            assertEquals(1, adjacencyList.outgoingEdgesCount(1))
            assertEquals(1, adjacencyList.outgoingEdgesCount(2))

            assertEquals(secondEdge.source(), adjacencyList.getEdge(0, 1).target())
            assertEquals(secondEdge.label(), adjacencyList.getEdge(0, 1).label())
            assertEquals(secondEdge.weight(), adjacencyList.getEdge(0, 1).weight())

            assertEquals(secondEdge.target(), adjacencyList.getEdge(2, 0).target())
            assertEquals(secondEdge.label(), adjacencyList.getEdge(2, 0).label())
            assertEquals(secondEdge.weight(), adjacencyList.getEdge(2, 0).weight())
        }
    }

    @Nested
    inner class VerticesCountTests {

        @Test
        fun `zero vertices`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            assertEquals(0, graph.verticesCount())
        }

        @Test
        fun `more than zero vertices`() {
            vertexValues.add("0")
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            assertEquals(1, graph.verticesCount())
        }
    }

    @Nested
    inner class IsWeightedTests {

        @Test
        fun `not weighted`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            assertFalse(graph.isWeighted())
        }

        @Test
        fun weighted() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            graph.weighted = true
            assertTrue(graph.isWeighted())
        }
    }

    @Nested
    inner class AddVertexTests {

        @Test
        fun `graph is immutable`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            graph.makeItLighterAndImmutable()
            assertFailsWith<IllegalArgumentException> { graph.addVertex("0") }
        }

        @Test
        fun `graph is empty`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            graph.addVertex("0")
            graph.makeItLighterAndImmutable()

            assertEquals(1, graph.verticesCount())
            assertEquals(arrayListOf("0"), vertexValues)
        }

        @Test
        fun `graph is not empty`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            graph.addVertex("0")
            graph.addVertex("1")
            graph.makeItLighterAndImmutable()

            assertEquals(2, graph.verticesCount())
            assertEquals(arrayListOf("0", "1"), vertexValues)
        }

        @Test
        fun `duplicate vertex`() {
            graph = UndirectedGraph(svsEdgesList, vertexValues)
            graph.addVertex("0")
            graph.addVertex("0")
            graph.makeItLighterAndImmutable()

            assertEquals(1, graph.verticesCount())
            assertEquals(arrayListOf("0"), vertexValues)
        }
    }

    @Nested
    inner class VertexValueTest {

        @Test
        fun `index of vertex is zero`() {
            vertexValues = arrayListOf("0")
            graph = UndirectedGraph(svsEdgesList, vertexValues)

            assertEquals("0", graph.vertexValue(0))
        }

        @Test
        fun `index of vertex is more than zero`() {
            vertexValues = arrayListOf("0", "1")
            graph = UndirectedGraph(svsEdgesList, vertexValues)

            assertEquals("1", graph.vertexValue(1))
        }

        @Test
        fun `index of vertex doesn't exist`() {
            vertexValues = arrayListOf("0")
            graph = UndirectedGraph(svsEdgesList, vertexValues)

            assertFailsWith<IllegalArgumentException> { graph.vertexValue(1) }
        }

        @Test
        fun `index of vertex is less than zero`() {
            vertexValues = arrayListOf("0")
            graph = UndirectedGraph(svsEdgesList, vertexValues)

            assertFailsWith<IllegalArgumentException> { graph.vertexValue(- 1) }
        }
    }

    @Nested
    inner class AddEdgeTests {
        private lateinit var expectedResult: SourceVertexStoringEdge
        private lateinit var svsList: List<SourceVertexStoringEdge>
        private val graph = UndirectedGraph()

        @Test
        fun `graph is immutable`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.makeItLighterAndImmutable()
            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1") }
        }

        @Test
        fun `source isn't in graph`() {
            graph.addVertex("0")
            assertFailsWith<IllegalArgumentException> { graph.addEdge("1", "0") }
        }

        @Test
        fun `target isn't in graph`() {
            graph.addVertex("0")
            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1") }
        }

        @Test
        fun `add edge in empty graph`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.addEdge("0", "1", "", -1.0)

            expectedResult = SourceVertexStoringEdge(0, 1, "", -1.0)
            svsList = graph.svsEdgesList()

            assertEquals(1, svsList.size)
            assertEquals(expectedResult.source(), svsList[0].source())
            assertEquals(expectedResult.target(), svsList[0].target())
            assertEquals(expectedResult.label(), svsList[0].label())
            assertEquals(expectedResult.weight(), svsList[0].weight())
        }

        @Test
        fun `add edge in not empty graph`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.addVertex("2")
            graph.addVertex("3")
            graph.addEdge("0", "1", "", -1.0)
            graph.addEdge("2", "3", "label", 2.0)

            expectedResult = SourceVertexStoringEdge(2, 3, "label", 2.0)
            svsList = graph.svsEdgesList()

            assertEquals(2, svsList.size)
            assertEquals(expectedResult.source(), svsList[1].source())
            assertEquals(expectedResult.target(), svsList[1].target())
            assertEquals(expectedResult.label(), svsList[1].label())
            assertEquals(expectedResult.weight(), svsList[1].weight())
        }

        @Test
        fun `add several edges to one vertex`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.addVertex("2")
            graph.addEdge("0", "1", "", -1.0)
            graph.addEdge("0", "2", "label", 1.0)

            expectedResult = SourceVertexStoringEdge(0, 2, "label", 1.0)
            svsList = graph.svsEdgesList()

            assertEquals(2, svsList.size)
            assertEquals(expectedResult.source(), svsList[1].source())
            assertEquals(expectedResult.target(), svsList[1].target())
            assertEquals(expectedResult.label(), svsList[1].label())
            assertEquals(expectedResult.weight(), svsList[1].weight())
        }

        @Test
        fun `add inverted edge is an error`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.addEdge("0", "1", "", -1.0)
            assertFailsWith<IllegalArgumentException> { graph.addEdge("1", "0", "", 2.0) }
        }

        @Test
        fun `duplicate edge`() {
            graph.addVertex("0")
            graph.addVertex("1")
            graph.addEdge("0", "1", "", -1.0)
            assertFailsWith<IllegalArgumentException> { graph.addEdge("0", "1", "label", 1.0) }
        }
    }
}
