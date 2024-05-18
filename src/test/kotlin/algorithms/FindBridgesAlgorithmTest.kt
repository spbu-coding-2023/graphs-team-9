package algorithms

import graph.UndirectedGraph
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class FindBridgesAlgorithmTest {
    private lateinit var graph: UndirectedGraph<Int>
    private lateinit var result: MutableSet<Set<Int>>
    private lateinit var expectedResult: MutableSet<Set<Int>>

    @BeforeEach
    fun setup() {
        graph = UndirectedGraph()
    }

    @AfterEach
    fun end() {
        assertEquals(expectedResult, result)
    }

    //
    @Test
    fun `graph is empty`() {
        result = graph.findBridges()
        expectedResult = mutableSetOf()
    }

    //  1   2   3
    @Test
    fun `no edges in graph`() {
        graph.addVertex(0)
        graph.addVertex(1)
        graph.addVertex(2)
        result = graph.findBridges()
        expectedResult = mutableSetOf()
    }

    //    0 — 1
//    |   |
//    2 — 3
    @Test
    fun `no bridges`() {
        graph.addVertex(0)
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addEdge(0, 1)
        graph.addEdge(1, 3)
        graph.addEdge(0, 2)
        graph.addEdge(2, 3)
        result = graph.findBridges()
        expectedResult = mutableSetOf()
    }

    //    0 — 1   7 — 6
//    |   |   |   |
//    2 — 3 — 4 — 5
    @Test
    fun `two components connected with bridge`() {
        graph.addVertex(0)
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addVertex(4)
        graph.addVertex(5)
        graph.addVertex(6)
        graph.addVertex(7)
        graph.addEdge(0, 1)
        graph.addEdge(1, 3)
        graph.addEdge(0, 2)
        graph.addEdge(2, 3)
        graph.addEdge(4, 5)
        graph.addEdge(4, 7)
        graph.addEdge(7, 6)
        graph.addEdge(5, 6)
        graph.addEdge(3, 4)
        result = graph.findBridges()
        expectedResult = mutableSetOf(setOf(3, 4))
    }

    //    0 — 1 — 2 — 3
//
    @Test
    fun `all edges - bridges`() {
        graph.addVertex(0)
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addEdge(0, 1)
        graph.addEdge(1, 2)
        graph.addEdge(2, 3)
        result = graph.findBridges()
        expectedResult = mutableSetOf(setOf(0, 1), setOf(1, 2), setOf(2, 3))
    }

    //    0 — 1   2 — 3
//
    @Test
    fun `several connectivity components`() {
        graph.addVertex(0)
        graph.addVertex(1)
        graph.addVertex(2)
        graph.addVertex(3)
        graph.addEdge(0, 1)
        graph.addEdge(2, 3)
        result = graph.findBridges()
        expectedResult = mutableSetOf(setOf(0, 1), setOf(2, 3))
    }
}
