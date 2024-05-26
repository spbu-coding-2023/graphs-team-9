package algorithms.layout

import graph.AdjacencyList
import kotlin.random.Random

/*
* source of realization:
* https://github.com/bhargavchippada/forceatlas2/tree/master
* GPL-3.0 license
 */

class ForceAtlas2(
    private val adjacencyList: AdjacencyList,
    private val strongGravityMode: Boolean = true,
) {
    private val verticesCount = adjacencyList.verticesCount()
    private val utils = LayoutUtils

    fun layout(
        iterations: Int = 1,
        randomStartPositionsMode: Boolean = false,
        scalingRatio: Double = 0.1,
        gravity: Double = 0.1,
    ): List<Pair<Double, Double>> {
        var speed = 1.0
        var speedEfficiency = 1.0
        val vertices = ArrayList<Vertex>()
        val delta = 2.0 / verticesCount
        for (vertexIndex in 0 until verticesCount) {
            val mass: Double = 1.0 + adjacencyList.outgoingEdgesCount(vertexIndex).toDouble()
            if (randomStartPositionsMode) {
                val (x, y) = Pair(Random.nextDouble(-1.0, 1.0), Random.nextDouble(-1.0, 1.0))
                vertices.add(Vertex(mass = mass, x = x, y = y))
            } else {
                val (x, y) = -1.0 + vertexIndex * delta to -1.0 + vertexIndex * 2 * delta
                vertices.add(Vertex(mass = mass, x = x, y = y))
            }
        }

        val outboundAttCompensation = 1.0
        var massSum = 0.0
        vertices.forEach {
            massSum += it.mass
        }
        repeat(iterations) {
            vertices.forEach { vertex ->
                vertex.oldDx = vertex.dx
                vertex.oldDy = vertex.dy
                vertex.dx = 0.0
                vertex.dy = 0.0
            }

            utils.applyRepulsion(vertices, scalingRatio)
            utils.applyGravity(vertices, gravity, scalingRatio, strongGravityMode)
            utils.applyAttraction(vertices, adjacencyList, outboundAttCompensation)
            val (newSpeed, newSpeedEfficiency) = utils.adjustSpeedAndApplyForces(vertices, speed, speedEfficiency)
            speed = newSpeed
            speedEfficiency = newSpeedEfficiency
        }
        return vertices.map { it.x to it.y }
    }
}
