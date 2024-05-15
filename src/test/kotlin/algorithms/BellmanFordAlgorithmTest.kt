package algorithms

import graph.UndirectedGraph
import org.junit.jupiter.api.*
import kotlin.test.assertEquals
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BellmanFordAlgorithmTest {
    private lateinit var undirectedGraph: UndirectedGraph<String>
    private var result: MutableList<Int>? = null

    @BeforeEach
    fun setup() {
        undirectedGraph = UndirectedGraph()
    }

    @Nested
    inner class TestsOnUndirectedGraph {

//           u  <- start/end
//         /   \
//        f  —  o
//
        @Test
        fun `path between the same vertex`() {
            undirectedGraph.addVertex("u")
            undirectedGraph.addVertex("f")
            undirectedGraph.addVertex("o")
            undirectedGraph.addEdge("u", "o")
            undirectedGraph.addEdge("u", "f")
            undirectedGraph.addEdge("f", "o")

            result = undirectedGraph.getShortestPathByBFAlgorithm("u", "u")
            val expectedResult = mutableListOf(0)
            assertEquals(expectedResult, result)
        }

//           r  <- start    end -> i
//         /   \                   |
//        u  —  s                  a
//
        @Test
        fun `path between unconnected vertices`() {
            undirectedGraph.addVertex("r")
            undirectedGraph.addVertex("u")
            undirectedGraph.addVertex("s")
            undirectedGraph.addVertex("i")
            undirectedGraph.addVertex("a")
            undirectedGraph.addEdge("r", "u")
            undirectedGraph.addEdge("r", "s")
            undirectedGraph.addEdge("u", "s")
            undirectedGraph.addEdge("i", "a")

            result = undirectedGraph.getShortestPathByBFAlgorithm("r", "i")
            assertNull(result)
        }

//            7
// start -> J — R <- end
//       3 /   / 1
//        V — C
//          2
//
        @Test
        fun `path between different connected vertices without choice`() {
            undirectedGraph.addVertex("J")
            undirectedGraph.addVertex("V")
            undirectedGraph.addVertex("C")
            undirectedGraph.addVertex("R")
            undirectedGraph.addEdge("J", "R", 7)
            undirectedGraph.addEdge("J", "V", 3)
            undirectedGraph.addEdge("V", "C", 2)
            undirectedGraph.addEdge("C", "R", 1)

            result = undirectedGraph.getShortestPathByBFAlgorithm("J", "R")
            val expectedResult = mutableListOf(0, 1, 2, 3)
            assertEquals(expectedResult, result)
        }

//            5   7
// start -> m — e — x <- end
//       3 /       / 1
//        a  —  t
//           8
//
        @Test
        fun `path between different connected vertices with choice`() {
            undirectedGraph.addVertex("m")
            undirectedGraph.addVertex("a")
            undirectedGraph.addVertex("t")
            undirectedGraph.addVertex("e")
            undirectedGraph.addVertex("x")
            undirectedGraph.addEdge("m", "a", 3)
            undirectedGraph.addEdge("a", "t", 8)
            undirectedGraph.addEdge("t", "x", 1)
            undirectedGraph.addEdge("m", "e", 5)
            undirectedGraph.addEdge("e", "x", 7)

            result = undirectedGraph.getShortestPathByBFAlgorithm("m", "x")
            val firstExpectedResult = mutableListOf(0, 3, 4)
            val secondExpectedResult = mutableListOf(0, 1, 2, 4)
            assertTrue(firstExpectedResult == result || secondExpectedResult == result)
        }

//        @Nested
//        inner class TestExceptions {
//            @Test
//            fun `both vertices do not exist`() {
//                undirectedGraph.addVertex("s")
//                result = undirectedGraph.getShortestPathByBFAlgorithm("v", "o")
//            }
//
//            @Test
//            fun `start-vertex do not exist`() {
//                undirectedGraph.addVertex("s")
//                result = undirectedGraph.getShortestPathByBFAlgorithm("s", "s")
//                expectedResult = mutableListOf(0)
//            }
//
//            @Test
//            fun `end-vertex do not exist`() {
//                undirectedGraph.addVertex("s")
//                result = undirectedGraph.getShortestPathByBFAlgorithm("s", "s")
//                expectedResult = mutableListOf(0)
//            }
//        }
    }
}