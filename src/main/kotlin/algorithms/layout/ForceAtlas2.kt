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
) {
    private val verticesCount = adjacencyList.verticesCount()
    private val utils = LayoutUtils
    private val outboundAttractionDistribution: Boolean = true

    fun layout(
        iterations: Int = 500,
        scalingRatio: Double = 2.0, // 15.0 for weighted, 2.0 for unweighted
        gravity: Double = 3.0, // 2.0 for weighted, 1.0 for unweighted
        strongGravityMode: Boolean = true,
        edgeWeightInfluence: Double = 1.0,
    ): List<Pair<Double, Double>> {
        var speed = 1.0
        var speedEfficiency = 1.0
        val vertices = ArrayList<Vertex>()
        for (vertexIndex in 0 until verticesCount) {
            val mass: Double = 1.0 + adjacencyList.outgoingEdgesCount(vertexIndex).toDouble()
            val (x, y) = Pair(Random.nextDouble(-1.0, 1.0), Random.nextDouble(-1.0, 1.0))
//            val (x, y) = Pair((1..5).random().toDouble(), (1..5).random().toDouble())
//            val (x, y) = Pair(random(), random())
            vertices.add(Vertex(mass = mass, x = x, y = y))
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
            utils.applyGravity(vertices, gravity, strongGravityMode, scalingRatio)
            utils.applyAttraction(vertices, adjacencyList, outboundAttractionDistribution, outboundAttCompensation, edgeWeightInfluence)
            val (newSpeed, newSpeedEfficiency) = utils.adjustSpeedAndApplyForces(vertices, speed, speedEfficiency)
            speed = newSpeed
            speedEfficiency = newSpeedEfficiency
        }
        val notNormalizeVertices = vertices.map { it.x to it.y }
        return notNormalizeVertices
    }
}
