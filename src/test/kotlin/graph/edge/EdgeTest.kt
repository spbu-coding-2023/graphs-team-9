package graph.edge

import graph.Edge
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals

class EdgeTest {
    private lateinit var edge: Edge

    @Test
    fun `return target`() {
        edge = Edge(0)
        val expectedResult = 0
        assertEquals(expectedResult, edge.target())
    }

    @Test
    fun `return label`() {
        edge = Edge(0, "label")
        val expectedResult = "label"
        assertEquals(expectedResult, edge.label())
    }

    @Test
    fun `return weight`() {
        edge = Edge(0, weight = 5.58)
        val expectedResult = 5.58
        assertEquals(expectedResult, edge.weight())
    }
}
