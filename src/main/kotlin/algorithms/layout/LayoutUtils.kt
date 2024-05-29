package algorithms.layout

import graph.AdjacencyList
import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

object LayoutUtils {
    private fun linRepulsion(
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
        vertices: ArrayList<Vertex>,
        coefficient: Double = 0.0,
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
        vertices: ArrayList<Vertex>,
        gravity: Double,
        useStrongGravity: Boolean = false,
        scalingRatio: Double = 0.0,
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
        vertices: ArrayList<Vertex>,
        adjacencyList: AdjacencyList,
        distributedAttraction: Boolean,
        coefficient: Double,
        edgeWeightInfluence: Double,
    ) {
        for (sourceVertexIndex in 0 until adjacencyList.verticesCount()) {
            for (outgoingEdgeIndex in 0 until adjacencyList.outgoingEdgesCount(sourceVertexIndex)) {
                val outgoingEdge = adjacencyList.getEdge(sourceVertexIndex, outgoingEdgeIndex)
                val targetVertexIndex = outgoingEdge.target()
                val outgoingEdgeWeight = outgoingEdge.weight()
                when (edgeWeightInfluence) {
                    0.0 -> {
                        linAttraction(
                            vertices[sourceVertexIndex],
                            vertices[targetVertexIndex],
                            1.0,
                            distributedAttraction,
                            coefficient,
                        )
                    }

                    1.0 -> {
                        linAttraction(
                            vertices[sourceVertexIndex],
                            vertices[targetVertexIndex],
                            outgoingEdgeWeight,
                            distributedAttraction,
                            coefficient,
                        )
                    }
                    else -> {
                        linAttraction(
                            vertices[sourceVertexIndex],
                            vertices[targetVertexIndex],
                            outgoingEdgeWeight.pow(edgeWeightInfluence),
                            distributedAttraction,
                            coefficient,
                        )
                    }
                }
            }
        }
    }

    fun adjustSpeedAndApplyForces(
        vertices: ArrayList<Vertex>,
        speed: Double,
        speedEfficiency: Double,
    ): Pair<Double, Double> {
        var totalSwinging = 0.0
        var totalEffectiveTraction = 0.0
        vertices.forEach { vertex ->
            val swinging = sqrt((vertex.oldDx - vertex.dx).pow(2) + (vertex.oldDy - vertex.dy).pow(2))
            totalSwinging += vertex.mass * swinging
            val effectiveTraction = 0.5 * sqrt((vertex.oldDx + vertex.dx).pow(2) + (vertex.oldDy + vertex.dy).pow(2))
            totalEffectiveTraction += vertex.mass * effectiveTraction
        }

        val estimatedOptimalJitterTolerance = .05 * sqrt(vertices.size.toDouble())
        val minJT = sqrt(estimatedOptimalJitterTolerance)
        val maxJT = sqrt(estimatedOptimalJitterTolerance)
        val jt =
            max(
                minJT,
                min(
                    maxJT,
                    estimatedOptimalJitterTolerance * totalEffectiveTraction / (
                        vertices.size * vertices.size
                    ),
                ),
            )
        val minSpeedEfficiency = 0.05
        var newSpeedEfficiency = speedEfficiency
        if ((totalEffectiveTraction > 2.0) && (totalSwinging / totalEffectiveTraction > 2.0)) {
            if (newSpeedEfficiency > minSpeedEfficiency) {
                newSpeedEfficiency *= 0.5
            }
        }

        val targetSpeed =
            if (totalSwinging == 0.0) {
                Double.MAX_VALUE
            } else {
                jt * newSpeedEfficiency * totalEffectiveTraction / totalSwinging
            }

        if ((totalSwinging > jt * totalEffectiveTraction) && (newSpeedEfficiency > minSpeedEfficiency)) {
            newSpeedEfficiency *= 0.7
        } else if (speed < 1000) {
            newSpeedEfficiency *= 1.3
        }

        val maxRise = 0.5
        val newSpeed = speed + min(targetSpeed - speed, maxRise * speed)
        vertices.forEach {
            val swinging = it.mass * sqrt((it.oldDx - it.dx).pow(2) + (it.oldDy - it.dy).pow(2))
            val factor = speed / (1.0 + sqrt(speed * swinging))
            it.x += (it.dx * factor)
            it.y += (it.dy * factor)
        }
        return newSpeed to minSpeedEfficiency
    }
}
