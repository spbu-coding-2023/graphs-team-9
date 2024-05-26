package graph

import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class SourceVertexStoringEdgeTest {
    private lateinit var edge: SourceVertexStoringEdge

    @Test
    fun `return source`() {
        edge = SourceVertexStoringEdge(0, 1)
        val expectedResult = 0
        assertEquals(expectedResult, edge.source())
    }

    @Test
    fun `return weight`() {
        edge = SourceVertexStoringEdge(0, 1, weight = 5.58)
        val expectedResult = 5.58
        assertEquals(expectedResult, edge.weight())
    }
}
