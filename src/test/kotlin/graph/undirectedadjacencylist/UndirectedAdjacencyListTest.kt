package graph.undirectedadjacencylist

import graph.Edge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

class UndirectedAdjacencyListTest {
    private lateinit var adjacencyList: UALForTests

    @Nested
    inner class AddVertexTests {

        @BeforeEach
        fun setup() {
            adjacencyList = UALForTests()
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
            adjacencyList = UALForTests()
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
            adjacencyList = UALForTests(0)
            assertEquals(arrayListOf(), adjacencyList.adjacencyList())
        }

        @Test
        fun `initialize with more than 0 vertices`() {
            adjacencyList = UALForTests(3)
            assertEquals(arrayListOf(arrayListOf(), arrayListOf(), arrayListOf()), adjacencyList.adjacencyList())
        }
    }

    @Nested
    inner class AddEdgeTests {

        @BeforeEach
        fun setup() {
            adjacencyList = UALForTests()
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

            val resultForFirstVertex = adjacencyList.adjacencyList()[0]
            val resultForSecondVertex = adjacencyList.adjacencyList()[1]
            assertEquals(1, resultForFirstVertex.size)
            assertEquals(1, resultForSecondVertex.size)

            val expectedResultForFirstVertex = Edge(1, "", 1.0)
            val expectedResultForSecondVertex = Edge(0, "", 1.0)
            val edgeForFirstVertex = resultForFirstVertex[0]
            val edgeForSecondVertex = resultForSecondVertex[0]

            assertEquals(expectedResultForFirstVertex.target(), edgeForFirstVertex.target())
            assertEquals(expectedResultForFirstVertex.label(), edgeForFirstVertex.label())
            assertEquals(expectedResultForFirstVertex.weight(), edgeForFirstVertex.weight())
            assertEquals(expectedResultForSecondVertex.target(), edgeForSecondVertex.target())
            assertEquals(expectedResultForSecondVertex.label(), edgeForSecondVertex.label())
            assertEquals(expectedResultForSecondVertex.weight(), edgeForSecondVertex.weight())
        }

        @Test
        fun `add edge in not empty adjacency list`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(1, 2, "label", 2.0)

            val resultForFirstVertex = adjacencyList.adjacencyList()[1]
            val resultForSecondVertex = adjacencyList.adjacencyList()[2]
            assertEquals(2, resultForFirstVertex.size)
            assertEquals(1, resultForSecondVertex.size)

            val expectedResultForFirstVertex = Edge(2, "label", 2.0)
            val expectedResultForSecondVertex = Edge(1, "label", 2.0)
            val edgeForFirstVertex = resultForFirstVertex[1]
            val edgeForSecondVertex = resultForSecondVertex[0]

            assertEquals(expectedResultForFirstVertex.target(), edgeForFirstVertex.target())
            assertEquals(expectedResultForFirstVertex.label(), edgeForFirstVertex.label())
            assertEquals(expectedResultForFirstVertex.weight(), edgeForFirstVertex.weight())
            assertEquals(expectedResultForSecondVertex.target(), edgeForSecondVertex.target())
            assertEquals(expectedResultForSecondVertex.label(), edgeForSecondVertex.label())
            assertEquals(expectedResultForSecondVertex.weight(), edgeForSecondVertex.weight())
        }

        @Test
        fun `add several edges to one vertex`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(0, 2, "label", 2.0)

            val resultForSource = adjacencyList.adjacencyList()[0]
            val resultForTarget = adjacencyList.adjacencyList()[2]
            assertEquals(2, resultForSource.size)
            assertEquals(1, resultForTarget.size)

            val expectedResultForSource = Edge(2, "label", 2.0)
            val expectedResultForTarget = Edge(0, "label", 2.0)
            val edgeForSource = resultForSource[1]
            val edgeForTarget = resultForTarget[0]

            assertEquals(expectedResultForSource.target(), edgeForSource.target())
            assertEquals(expectedResultForSource.label(), edgeForSource.label())
            assertEquals(expectedResultForSource.weight(), edgeForSource.weight())
            assertEquals(expectedResultForTarget.target(), edgeForTarget.target())
            assertEquals(expectedResultForTarget.label(), edgeForTarget.label())
            assertEquals(expectedResultForTarget.weight(), edgeForTarget.weight())
        }

        @Test
        fun `add inverted edge is an error`() {
            adjacencyList.addVertex()
            adjacencyList.addVertex()
            adjacencyList.addEdge(0, 1, "", 1.0)
            assertFailsWith<IllegalArgumentException> { adjacencyList.addEdge(1, 0, "label", 2.0) }
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
            adjacencyList = UALForTests()
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
            adjacencyList = UALForTests()
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
