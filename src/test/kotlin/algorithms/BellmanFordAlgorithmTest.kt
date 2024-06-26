package algorithms

import graph.DirectedGraph
import graph.UndirectedGraph
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull
import kotlin.test.assertTrue

class BellmanFordAlgorithmTest {
    private var result: MutableList<Int>? = null

    @Nested
    inner class TestsOnUndirectedGraph {
        private var undirectedGraph = UndirectedGraph()

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
            undirectedGraph.makeItLighterAndImmutable()

            result = undirectedGraph.shortestPathByBFAlgorithm("u", "u")
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
            undirectedGraph.makeItLighterAndImmutable()

            result = undirectedGraph.shortestPathByBFAlgorithm("r", "i")
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
            undirectedGraph.addEdge("J", "R", weight = 7.0)
            undirectedGraph.addEdge("J", "V", weight = 3.0)
            undirectedGraph.addEdge("V", "C", weight = 2.0)
            undirectedGraph.addEdge("C", "R", weight = 1.0)
            undirectedGraph.makeItLighterAndImmutable()

            result = undirectedGraph.shortestPathByBFAlgorithm("J", "R")
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
            undirectedGraph.addEdge("m", "a", weight = 3.0)
            undirectedGraph.addEdge("a", "t", weight = 8.0)
            undirectedGraph.addEdge("t", "x", weight = 1.0)
            undirectedGraph.addEdge("m", "e", weight = 5.0)
            undirectedGraph.addEdge("e", "x", weight = 7.0)
            undirectedGraph.makeItLighterAndImmutable()

            result = undirectedGraph.shortestPathByBFAlgorithm("m", "x")
            val firstExpectedResult = mutableListOf(0, 3, 4)
            val secondExpectedResult = mutableListOf(0, 1, 2, 4)
            assertTrue(firstExpectedResult == result || secondExpectedResult == result)
        }

        @Nested
        inner class TestExceptionsInUndirectedGraph {
            @Test
            fun `both vertices do not exist`() {
                undirectedGraph.addVertex("s")
                undirectedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { undirectedGraph.shortestPathByBFAlgorithm("v", "o") }
            }

            @Test
            fun `start-vertex do not exist`() {
                undirectedGraph.addVertex("s")
                undirectedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { undirectedGraph.shortestPathByBFAlgorithm("m", "s") }
            }

            @Test
            fun `end-vertex do not exist`() {
                undirectedGraph.addVertex("s")
                undirectedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { undirectedGraph.shortestPathByBFAlgorithm("s", "k") }
            }

//           d  <- start
//      -4 /   \ 3
//        r  —  e  <- end
//           -2
//
            @Test
            fun `path with negative cycle`() {
                undirectedGraph.addVertex("d")
                undirectedGraph.addVertex("r")
                undirectedGraph.addVertex("e")
                undirectedGraph.addEdge("d", "r", weight = -4.0)
                undirectedGraph.addEdge("d", "e", weight = 3.0)
                undirectedGraph.addEdge("r", "e", weight = -2.0)
                undirectedGraph.makeItLighterAndImmutable()
                assertFailsWith<UnsupportedOperationException> { undirectedGraph.shortestPathByBFAlgorithm("d", "e") }
            }
        }
    }

    @Nested
    inner class TestsOnDirectedGraph {
        private var directedGraph = DirectedGraph()

//           u  <- start/end
//         ↙   ↘
//        f  —> o
//
        @Test
        fun `path between the same vertex`() {
            directedGraph.addVertex("u")
            directedGraph.addVertex("f")
            directedGraph.addVertex("o")
            directedGraph.addEdge("u", "o")
            directedGraph.addEdge("u", "f")
            directedGraph.addEdge("f", "o")
            directedGraph.makeItLighterAndImmutable()

            result = directedGraph.shortestPathByBFAlgorithm("u", "u")
            val expectedResult = mutableListOf(0)
            assertEquals(expectedResult, result)
        }

//           r  <- start    end -> i
//         ↙   ↘                   ↓
//        u  —> s                  a
//
        @Test
        fun `path between unconnected vertices`() {
            directedGraph.addVertex("r")
            directedGraph.addVertex("u")
            directedGraph.addVertex("s")
            directedGraph.addVertex("i")
            directedGraph.addVertex("a")
            directedGraph.addEdge("r", "u")
            directedGraph.addEdge("r", "s")
            directedGraph.addEdge("u", "s")
            directedGraph.addEdge("i", "a")
            directedGraph.makeItLighterAndImmutable()

            result = directedGraph.shortestPathByBFAlgorithm("r", "i")
            assertNull(result)
        }

//           d  <- start
//       4 ↙   ↘ 3
//        r  —> e  <- end
//           -2
//
        @Test
        fun `path with negative edge weights`() {
            directedGraph.addVertex("d")
            directedGraph.addVertex("r")
            directedGraph.addVertex("e")
            directedGraph.addEdge("d", "r", weight = 4.0)
            directedGraph.addEdge("d", "e", weight = 3.0)
            directedGraph.addEdge("r", "e", weight = -2.0)
            directedGraph.makeItLighterAndImmutable()

            result = directedGraph.shortestPathByBFAlgorithm("d", "e")
            val expectedResult = mutableListOf(0, 1, 2)
            assertEquals(expectedResult, result)
        }

//             -2   5
// start ->  m —> e —> x <- end
//       -3 ↙        ↗ -2
//         a   —>  t
//             8
//
        @Test
        fun `path between different connected vertices with choice`() {
            directedGraph.addVertex("m")
            directedGraph.addVertex("a")
            directedGraph.addVertex("t")
            directedGraph.addVertex("e")
            directedGraph.addVertex("x")
            directedGraph.addEdge("m", "a", weight = -3.0)
            directedGraph.addEdge("a", "t", weight = 8.0)
            directedGraph.addEdge("t", "x", weight = -2.0)
            directedGraph.addEdge("m", "e", weight = -2.0)
            directedGraph.addEdge("e", "x", weight = 5.0)
            directedGraph.makeItLighterAndImmutable()

            result = directedGraph.shortestPathByBFAlgorithm("m", "x")
            val firstExpectedResult = mutableListOf(0, 3, 4)
            val secondExpectedResult = mutableListOf(0, 1, 2, 4)
            assertTrue(firstExpectedResult == result || secondExpectedResult == result)
        }

        @Nested
        inner class TestExceptionsInDirectedGraph {
            @Test
            fun `both vertices do not exist`() {
                directedGraph.addVertex("s")
                directedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { directedGraph.shortestPathByBFAlgorithm("v", "o") }
            }

            @Test
            fun `start-vertex do not exist`() {
                directedGraph.addVertex("s")
                directedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { directedGraph.shortestPathByBFAlgorithm("m", "s") }
            }

            @Test
            fun `end-vertex do not exist`() {
                directedGraph.addVertex("s")
                directedGraph.makeItLighterAndImmutable()
                assertFailsWith<IllegalArgumentException> { directedGraph.shortestPathByBFAlgorithm("s", "k") }
            }

//              k <- end
//              ↑ 1
//              n
//         -1 ↙  ↖ 2
// start ->  p —> u
//             -2
            @Test
            fun `path with negative cycle`() {
                directedGraph.addVertex("p")
                directedGraph.addVertex("u")
                directedGraph.addVertex("n")
                directedGraph.addVertex("k")
                directedGraph.addEdge("p", "u", weight = -2.0)
                directedGraph.addEdge("u", "n", weight = 2.0)
                directedGraph.addEdge("n", "k", weight = 1.0)
                directedGraph.addEdge("n", "p", weight = -1.0)
                directedGraph.makeItLighterAndImmutable()
                assertFailsWith<UnsupportedOperationException> { directedGraph.shortestPathByBFAlgorithm("p", "k") }
            }
        }
    }
}
