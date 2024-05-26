package graph.directedadjacencylist

import graph.Edge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class DirectedAdjacencyListTest {
    private lateinit var adjacencyList: DALForTests

    @Nested
    inner class AddVertexTests {

        @BeforeEach
        fun setup() {
            adjacencyList = DALForTests()
        }

        @Test
        fun `empty adjacency list`() {
            assertEquals(0, adjacencyList.addVertex())
            val expectedResult: ArrayList<ArrayList<Edge>> = arrayListOf(arrayListOf())
            assertEquals(expectedResult, adjacencyList.adjacencyList())
        }

        @Test
        fun `not empty adjacency list`() {
            adjacencyList.addVertex()
            assertEquals(1, adjacencyList.addVertex())
            val expectedResult: ArrayList<ArrayList<Edge>> = arrayListOf(arrayListOf(), arrayListOf())
            assertEquals(expectedResult, adjacencyList.adjacencyList())
        }
    }

    @Nested
    inner class VerticesCountTests {

        @BeforeEach
        fun setup() {
            adjacencyList = DALForTests()
        }

        @Test
        fun `zero vertices`() {
            assertEquals(0, adjacencyList.verticesCount())
        }

        @Test
        fun `more than zero vertices`() {
            adjacencyList.addVertex()
            assertEquals(1, adjacencyList.verticesCount())
        }
    }

    @Nested
    inner class InitializationTests {

        @Test
        fun `initialize with 0 vertices`() {
            adjacencyList = DALForTests(0)
            assertEquals(arrayListOf(), adjacencyList.adjacencyList())
        }

        @Test
        fun `initialize with more than 0 vertices`() {
            adjacencyList = DALForTests(3)
            assertEquals(arrayListOf(arrayListOf(), arrayListOf(), arrayListOf()), adjacencyList.adjacencyList())
        }
    }

    @Nested
    inner class AddEdgeTests {

        @BeforeEach
        fun setup() {
            adjacencyList = DALForTests()
        }

        @Test
        fun `source not in adjacency list`() {
            adjacencyList.addVertex()
            assertFailsWith<IllegalArgumentException> { adjacencyList.addEdge(1, 0, "", 1.0) }
        }

        @Test
        fun `target not in adjacency list`() {
            adjacencyList.addVertex()
            assertFailsWith<IllegalArgumentException> { adjacencyList.addEdge(0, 1, "", 1.0) }
        }

        @Test
        fun `add edge in empty adjacency list`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            val expectedResult = Edge(1, "", 1.0)
            val result = adjacencyList.adjacencyList()[0]
            assertEquals(1, result.size)
            val edge = result[0]
            assertEquals(expectedResult.target(), edge.target())
            assertEquals(expectedResult.label(), edge.label())
            assertEquals(expectedResult.weight(), edge.weight())
        }

        @Test
        fun `add edge in not empty adjacency list`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(1, 0, "label", 2.0)
            val expectedResult = Edge(0, "label", 2.0)
            val result = adjacencyList.adjacencyList()[1]
            assertEquals(1, result.size)
            val edge = result[0]
            assertEquals(expectedResult.target(), edge.target())
            assertEquals(expectedResult.label(), edge.label())
            assertEquals(expectedResult.weight(), edge.weight())
        }

        @Test
        fun `add several edges to one vertex`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(0, 2, "label", 2.0)
            val expectedResult = Edge(2, "label", 2.0)
            val result = adjacencyList.adjacencyList()[0]
            assertEquals(2, result.size)
            val edge = result[1]
            assertEquals(expectedResult.target(), edge.target())
            assertEquals(expectedResult.label(), edge.label())
            assertEquals(expectedResult.weight(), edge.weight())
        }

        @Test
        fun `duplicate edge`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            assertFailsWith<IllegalArgumentException> { adjacencyList.addEdge(0, 1, "label", 2.0) }
        }
    }

    @Nested
    inner class OutgoingEdgesCountTests {

        @BeforeEach
        fun setup() {
            adjacencyList = DALForTests()
        }

        @Test
        fun `source not in adjacency list`() {
            adjacencyList.addVertex()
            assertFailsWith<IllegalArgumentException> { adjacencyList.outgoingEdgesCount(1) }
        }

        @Test
        fun `zero outgoing edges`() {
            adjacencyList.addVertex()
            assertEquals(0, adjacencyList.outgoingEdgesCount(0))
        }

        @Test
        fun `more than zero outgoing edges`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            assertEquals(1, adjacencyList.outgoingEdgesCount(0))
        }
    }

    @Nested
    inner class GetEdgeTests {

        @BeforeEach
        fun setup() {
            adjacencyList = DALForTests()
        }

        @Test
        fun `source not in adjacency list`() {
            assertFailsWith<IllegalArgumentException>{ adjacencyList.getEdge(0, 1) }
        }

        @Test
        fun `edge doesn't exist`() {
            adjacencyList.addVertex()
            assertFailsWith<IllegalArgumentException>{ adjacencyList.getEdge(0, 0) }
        }

        @Test
        fun `edge with index = zero`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            val expectedResult = Edge(1, "", 1.0)
            val result = adjacencyList.getEdge(0, 0)
            assertEquals(expectedResult.target(), result.target())
            assertEquals(expectedResult.label(), result.label())
            assertEquals(expectedResult.weight(), result.weight())
        }

        @Test
        fun `edge with index more than zero`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(0, 2, "label", 2.0)
            val expectedResult = Edge(2, "label", 2.0)
            val result = adjacencyList.getEdge(0, 1)
            assertEquals(expectedResult.target(), result.target())
            assertEquals(expectedResult.label(), result.label())
            assertEquals(expectedResult.weight(), result.weight())
        }
    }
}