package graph

import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNotNull

class DirectedGraphTest {
    private lateinit var adjacencyList: DirectedAdjacencyList
    private lateinit var vertexValues: ArrayList<Int>
    private lateinit var graph: DirectedGraph<Int>

    @Nested
    inner class InitializationTests {

        @Test
        fun `size of vertex values list more than amount of vertices in adjacency list`() {
            adjacencyList = DirectedAdjacencyList()
            vertexValues = arrayListOf(0, 1)
            assertFailsWith<IllegalArgumentException> { DirectedGraph(adjacencyList, vertexValues) }
        }

        @Test
        fun `size of vertex values list less than amount of vertices in adjacency list`() {
            adjacencyList = DirectedAdjacencyList(2)
            vertexValues = arrayListOf()
            assertFailsWith<IllegalArgumentException> { DirectedGraph(adjacencyList, vertexValues) }
        }

        @Test
        fun `size of vertex values list and amount of vertices in adjacency list equal to zero`() {
            adjacencyList = DirectedAdjacencyList()
            vertexValues = arrayListOf()
            assertNotNull(DirectedGraph(adjacencyList, vertexValues))
        }

        @Test
        fun `size of vertex values list equal to amount of vertices in adjacency list and more than zero`() {
            adjacencyList = DirectedAdjacencyList(2)
            vertexValues = arrayListOf(0, 1)
            assertNotNull(DirectedGraph(adjacencyList, vertexValues))
        }
    }

    @Nested
    inner class AdjacencyListTests {

        @Test
        fun `empty list`() {
            adjacencyList = DirectedAdjacencyList()
            vertexValues = arrayListOf()
            graph = DirectedGraph(adjacencyList, vertexValues)
            assertEquals(adjacencyList, graph.adjacencyList())
        }

        @Test
        fun `list with one vertex`() {
            adjacencyList = DirectedAdjacencyList(1)
            vertexValues = arrayListOf(0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            assertEquals(adjacencyList, graph.adjacencyList())
        }

        @Test
        fun `list with more than one vertices`() {
            adjacencyList = DirectedAdjacencyList(3)
            vertexValues = arrayListOf(0, 1, 2)
            graph = DirectedGraph(adjacencyList, vertexValues)
            assertEquals(adjacencyList, graph.adjacencyList())
        }

        @Test
        fun `list with one edge`() {
            adjacencyList = DirectedAdjacencyList(2)
            adjacencyList.addEdge(0, 1, "", 1.0)
            vertexValues = arrayListOf(0, 1)
            graph = DirectedGraph(adjacencyList, vertexValues)
            assertEquals(adjacencyList, graph.adjacencyList())
        }

        @Test
        fun `list with more than one edges`() {
            adjacencyList = DirectedAdjacencyList(3)
            vertexValues = arrayListOf(0, 1, 2)
            adjacencyList.addEdge(0, 1, "label", 1.0)
            adjacencyList.addEdge(1, 0, "laba", 3.0)
            adjacencyList.addEdge(0, 2, "", 2.0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            assertEquals(adjacencyList, graph.adjacencyList())
        }
    }

    @Nested
    inner class SVSEdgesListTests {
        private lateinit var expectedResult: List<SourceVertexStoringEdge>
        private lateinit var result: List<SourceVertexStoringEdge>

        @Test
        fun `empty svs list`() {
            adjacencyList = DirectedAdjacencyList(1)
            vertexValues = arrayListOf(0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            expectedResult = arrayListOf()
            result = graph.svsEdgesList()
            assertEquals(expectedResult, result)
        }

        @Test
        fun `svs list with more than zero edges`() {
            adjacencyList = DirectedAdjacencyList(2)
            vertexValues = arrayListOf(0, 1)
            adjacencyList.addEdge(0, 1, "", 1.0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            expectedResult = arrayListOf(SourceVertexStoringEdge(0, 1, "", 1.0))
            result = graph.svsEdgesList()

            assertEquals(expectedResult.size, result.size)
            assertEquals(expectedResult[0].source(), result[0].source())
            assertEquals(expectedResult[0].target(), result[0].target())
            assertEquals(expectedResult[0].label(), result[0].label())
            assertEquals(expectedResult[0].weight(), result[0].weight())
        }

        @Test
        fun `svs list with several edges from one vertex`() {
            adjacencyList = DirectedAdjacencyList(3)
            vertexValues = arrayListOf(0, 1, 2)
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(0, 2, "label", 2.0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            expectedResult = arrayListOf(SourceVertexStoringEdge(0, 1, "", 1.0),
                SourceVertexStoringEdge(0, 2, "label", 2.0))
            result = graph.svsEdgesList()

            assertEquals(expectedResult.size, result.size)

            assertEquals(expectedResult[0].source(), result[0].source())
            assertEquals(expectedResult[0].target(), result[0].target())
            assertEquals(expectedResult[0].label(), result[0].label())
            assertEquals(expectedResult[0].weight(), result[0].weight())

            assertEquals(expectedResult[1].source(), result[1].source())
            assertEquals(expectedResult[1].target(), result[1].target())
            assertEquals(expectedResult[1].label(), result[1].label())
            assertEquals(expectedResult[1].weight(), result[1].weight())
        }

        @Test
        fun `svs list where inverted edges are two different edges`() {
            adjacencyList = DirectedAdjacencyList(2)
            vertexValues = arrayListOf(0, 1)
            adjacencyList.addEdge(0, 1, "", 1.0)
            adjacencyList.addEdge(1, 0, "label", 2.0)
            graph = DirectedGraph(adjacencyList, vertexValues)
            expectedResult = arrayListOf(SourceVertexStoringEdge(0, 1, "", 1.0),
                SourceVertexStoringEdge(1, 0, "label", 2.0))
            result = graph.svsEdgesList()

            assertEquals(expectedResult.size, result.size)

            assertEquals(expectedResult[0].source(), result[0].source())
            assertEquals(expectedResult[0].target(), result[0].target())
            assertEquals(expectedResult[0].label(), result[0].label())
            assertEquals(expectedResult[0].weight(), result[0].weight())

            assertEquals(expectedResult[1].source(), result[1].source())
            assertEquals(expectedResult[1].target(), result[1].target())
            assertEquals(expectedResult[1].label(), result[1].label())
            assertEquals(expectedResult[1].weight(), result[1].weight())
        }
    }
}