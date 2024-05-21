package algorithms.layout

import kotlin.math.pow
import kotlin.math.sqrt

class FA2Utils() {
    fun linRepulsion(
        vertex1: Vertex,
        vertex2: Vertex,
        coefficient: Double = 0.0,
    ) {
        val xDist = vertex1.x - vertex2.x
        val yDist = vertex1.y - vertex2.y
        val distanceSquared = xDist * xDist + yDist * yDist

        if (distanceSquared > 0) {
            val factor = coefficient * vertex1.mass * vertex2.mass / distanceSquared
            vertex1.dx += xDist * factor
            vertex1.dy += yDist * factor
            vertex2.dx -= xDist * factor
            vertex2.dy -= yDist * factor
        }
    }

    fun linRepulsionRegion(
        vertex: Vertex,
        region: Region,
        coefficient: Double = 0.0,
    ) {
        val xDist = vertex.x - region.massCenterX
        val yDist = vertex.y - region.massCenterY
        val distanceSquared = xDist * xDist + yDist * yDist

        if (distanceSquared > 0) {
            val factor = coefficient * vertex.mass * region.mass / distanceSquared
            vertex.dx += xDist * factor
            vertex.dy += yDist * factor
        }
    }

    private fun linGravity(
        vertex: Vertex,
        g: Double,
    ) {
        val xDist = vertex.x
        val yDist = vertex.y
        val distanceSquared = sqrt(xDist * xDist + yDist * yDist)

        if (distanceSquared > 0) {
            val factor = vertex.mass * g / distanceSquared
            vertex.dx -= xDist * factor
            vertex.dy -= yDist * factor
        }
    }

    private fun strongGravity(
        vertex: Vertex,
        g: Double,
        coefficient: Double = 0.0,
    ) {
        val xDist = vertex.x
        val yDist = vertex.y

        if (xDist != 0.0 && yDist != 0.0) {
            val factor = coefficient * vertex.mass * g
            vertex.dx -= xDist * factor
            vertex.dy -= yDist * factor
        }
    }

    private fun linAttraction(
        vertex1: Vertex,
        vertex2: Vertex,
        e: Double,
        distributedAttraction: Boolean,
        coefficient: Double = 0.0,
    ) {
        val xDist = vertex1.x - vertex2.x
        val yDist = vertex1.y - vertex2.y
        val factor =
            if (!distributedAttraction) {
                -coefficient * e
            } else {
                -coefficient * e / vertex1.mass
            }
        vertex1.dx += xDist * factor
        vertex1.dy += yDist * factor
        vertex2.dx -= xDist * factor
        vertex2.dy -= yDist * factor
    }

    fun applyRepulsion(
        vertices: List<Vertex>,
        coefficient: Double,
    ) {
        for ((i, vertex1) in vertices.withIndex()) {
            var j = i
            for (vertex2 in vertices) {
                if (j == 0) break
                linRepulsion(vertex1, vertex2, coefficient)
                j--
            }
        }
    }

    fun applyGravity(
        vertices: List<Vertex>,
        gravity: Double,
        scalingRatio: Double,
        useStrongGravity: Boolean = false,
    ) {
        if (!useStrongGravity) {
            for (vertex in vertices) {
                linGravity(vertex, gravity)
            }
        } else {
            for (vertex in vertices) {
                strongGravity(vertex, gravity, scalingRatio)
            }
        }
    }

    fun applyAttraction(
        vertices: List<Vertex>,
        edges: List<Edge>,
        distributedAttraction: Boolean,
        coefficient: Double,
        edgeWeightInfluence: Double,
    ) {
        when (edgeWeightInfluence) {
            0.0 -> {
                for (edge in edges) {
                    linAttraction(vertices[edge.sourceVertex], vertices[edge.targetVertex], 1.0, distributedAttraction, coefficient)
                }
            }
            1.0 -> {
                for (edge in edges) {
                    linAttraction(vertices[edge.sourceVertex], vertices[edge.targetVertex], edge.weight, distributedAttraction, coefficient)
                }
            }
            else -> {
                for (edge in edges) {
                    linAttraction(
                        vertices[edge.sourceVertex],
                        vertices[edge.targetVertex],
                        edge.weight.pow(edgeWeightInfluence),
                        distributedAttraction,
                        coefficient,
                    )
                }
            }
        }
    }
}
