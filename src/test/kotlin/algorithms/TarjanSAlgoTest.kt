package algorithms

import graph.DirectedAdjacencyList
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class TarjanSAlgoTest {
    lateinit var adjList: DirectedAdjacencyList

    @BeforeEach
    fun setup() {
        adjList = DirectedAdjacencyList()
    }

    //    -> '0' -> '1' -> '2' -
//    |                    |
//    |------------------- |
    @Test
    fun `loop graph`() {
        adjList.apply {
            for (vertex in 0..2) {
                addVertex()
            }
            for (vertex in 0..2) {
                addEdge(vertex, (vertex + 1).mod(3), "", 1.0)
            }
        }
        val res = TarjanSAlgo(adjList).tarjanSAlgo()
        assert(res.size == 1)
        assert(res[0].size == 3)
        assert(res[0].containsAll(listOf(0, 1, 2)))
    }

    //     - '0' -> '1' -> '2'<-
//    |                    |
//    |------------------- |
    @Test
    fun `vertices connected but not strongly connected`() {
        adjList.apply {
            for (vertex in 0..2) {
                addVertex()
            }
            addEdge(0, 1, "", 1.0)
            addEdge(1, 2, "", 1.0)
            addEdge(0, 2, "", 1.0)
        }
        val res = TarjanSAlgo(adjList).tarjanSAlgo()
        assert(res.size == 3)
        assert(res[0].size == res[1].size && res[1].size == res[2].size && res[0].size == 1)
        assert(res[0][0] != res[1][0] && res[2][0] != res[1][0] && res[0][0] != res[2][0])
    }

//      '5' <-|-> '6'
//    '2' <- '0' <- '1' <--
//     '3' <- | -> '4' ---|

    @Test
    fun `vertex has a lot of outgoing edges`() {
        adjList.apply {
            for (vertex in 0..6) {
                addVertex()
                if (vertex != 1) {
                    addEdge(0, vertex, "", 1.0)
                }
            }
            addEdge(4, 1, "", 1.0)
            addEdge(1, 0, "", 1.0)
        }
        val res = TarjanSAlgo(adjList).tarjanSAlgo()
        val sizes = res.map { it.size }
        assert(sizes.contains(3) && sizes.count { it == 1 } == 4)
    }

//     --->'2'----
//     |         |
//     |---|     |
//         |     |
//     -->'1'<----
//     |  |
//     |  |------
//     |        |
//     --'0'<---|

    @Test
    fun `8-like graph`() {
        adjList.apply {
            for (vertex in 0..2) {
                addVertex()
            }
            addEdge(0, 1, "", 1.0)
            addEdge(1, 2, "", 1.0)
            addEdge(2, 1, "", 1.0)
            addEdge(1, 0, "", 1.0)
        }
        val res = TarjanSAlgo(adjList).tarjanSAlgo()
        assert(res.size == 1)
        assert(res[0].toSet() == setOf(0, 1, 2))
    }

    //    '0' -> '1' <----        '2' -> '3' <---|
//    |--------------|         |-------------|
    @Test
    fun `two disconnected scc`() {
        adjList.apply {
            for (vertex in 0..3) {
                addVertex()
            }
            addEdge(0, 1, "", 1.0)
            addEdge(1, 0, "", 1.0)
            addEdge(2, 3, "", 1.0)
            addEdge(3, 2, "", 1.0)
        }
        val res = TarjanSAlgo(adjList).tarjanSAlgo()
        assert(res.size == 2)
        assert(res[0].toSet() == setOf(0, 1) && res[1].toSet() == setOf(2, 3))
    }
}
