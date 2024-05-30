package algorithms

import graph.SourceVertexStoringEdge
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import kotlin.test.assertFailsWith

class BoruvkaSAlgorithmTest {
    @Nested
    inner class TestsOnUnionFind {
        private lateinit var unionFind: UnionFind

        @BeforeEach
        fun setup() {
            unionFind = UnionFind(5)
        }

        @Test
        fun `find after unite call`() {
            unionFind.unite(0, 1)
            assert(unionFind.find(0) == unionFind.find(1))
        }

        @Test
        fun `find after two unite calls`() {
            unionFind.unite(0, 1)
            unionFind.unite(1, 2)
            assert(unionFind.find(0) == unionFind.find(2))
            assert(unionFind.find(1) == unionFind.find(2))
        }

        @Test
        fun `find after two unite calls (reversed)`() {
            unionFind.unite(1, 0)
            unionFind.unite(2, 1)
            assert(unionFind.find(0) == unionFind.find(2))
            assert(unionFind.find(1) == unionFind.find(2))
        }

        @Test
        fun `find after a few unite calls`() {
            unionFind.unite(0, 1)
            unionFind.unite(1, 2)
            unionFind.unite(3, 2)
            unionFind.unite(3, 4)
            assert(unionFind.find(0) == unionFind.find(4))
            assert(unionFind.find(1) == unionFind.find(3))
            assert(unionFind.find(2) == unionFind.find(0))
            assert(unionFind.find(2) == unionFind.find(3))
        }
    }

//        1
//    '0'---'1'
//     \    |
//   1  \  | 1
//       '2'
    @Test
    fun `right triangle`() {
        val edgesList =
            listOf(
                SourceVertexStoringEdge(0, 1, weight = 1.0),
                SourceVertexStoringEdge(1, 2, weight = 1.0),
                SourceVertexStoringEdge(2, 0, weight = 1.0),
            )
        assert(BoruvkaSAlgorithm(edgesList, 3).boruvkaSAlgo().size == 2)
    }

//        1     1    1    1
//    '0' - '1' - '2'- '3'- '4'
//     |                     |
//     |---------------------|
//                5

    @Test
    fun `one connected component`() {
        val edgesList =
            listOf(
                SourceVertexStoringEdge(0, 1, weight = 1.0),
                SourceVertexStoringEdge(1, 2, weight = 1.0),
                SourceVertexStoringEdge(2, 3, weight = 1.0),
                SourceVertexStoringEdge(3, 4, weight = 1.0),
                SourceVertexStoringEdge(0, 4, weight = 5.0),
            )
        val res = BoruvkaSAlgorithm(edgesList, 5).boruvkaSAlgo()
        assert(
            res.map { setOf(it.source(), it.target()) }.toSet() ==
                setOf(setOf(0, 1), setOf(1, 2), setOf(2, 3), setOf(3, 4)),
        )
    }

//       -2     -2   -1   -2
//    '0' - '1' - '2'- '3'- '4'
//     |                     |
//     |---------------------|
//                -5

    @Test
    fun `one connected component (negative weights)`() {
        val edgesList =
            listOf(
                SourceVertexStoringEdge(0, 1, weight = -2.0),
                SourceVertexStoringEdge(1, 2, weight = -2.0),
                SourceVertexStoringEdge(2, 3, weight = -1.0),
                SourceVertexStoringEdge(3, 4, weight = -2.0),
                SourceVertexStoringEdge(0, 4, weight = -5.0),
            )
        val res = BoruvkaSAlgorithm(edgesList, 5).boruvkaSAlgo()
        assert(
            res.map { setOf(it.source(), it.target()) }.toSet() ==
                setOf(setOf(0, 1), setOf(1, 2), setOf(4, 3), setOf(0, 4)),
        )
    }

//        -1        1
//    '0'---'1' '2'---'3' '4'
    @Test
    fun `disconnected graph`() {
        val edgesList =
            listOf(
                SourceVertexStoringEdge(0, 1, weight = -1.0),
                SourceVertexStoringEdge(2, 3, weight = 1.0),
            )
        val res = BoruvkaSAlgorithm(edgesList, 5).boruvkaSAlgo()
        assert(
            res.map { setOf(it.source(), it.target()) }.toSet() ==
                setOf(setOf(0, 1), setOf(3, 2)),
        )
    }

    @Test
    fun `illegal source vertex`() {
        assertFailsWith<IllegalArgumentException> {
            BoruvkaSAlgorithm(listOf(SourceVertexStoringEdge(1, 0)), 1).boruvkaSAlgo()
        }
    }

    @Test
    fun `illegal target vertex`() {
        assertFailsWith<IllegalArgumentException> {
            BoruvkaSAlgorithm(listOf(SourceVertexStoringEdge(0, 1)), 1).boruvkaSAlgo()
        }
    }
}
