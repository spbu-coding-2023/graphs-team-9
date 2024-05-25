package algorithms.layout

import graph.AdjacencyList
import graph.Graph
import kotlin.random.Random

/*
* source of realization:
* https://github.com/bhargavchippada/forceatlas2/tree/master
* GPL-3.0 license
 */
class ForceAtlas2<V>(
    private val outboundAttractionDistribution: Boolean = true,
    private val edgeWeightInfluence: Double = 1.0,
    private val jitterTolerance: Double = 1.0,
    private val barnesHutOptimize: Boolean = false,
    private val barnesHutTheta: Double = 1.2,
    private val scalingRatio: Double = 2.0,
    private val strongGravityMode: Boolean = false,
    private val gravity: Double = 1.0,
) {
    private val utils = LayoutUtils

    fun layout(
        graph: Graph<V>,
        iterations: Int = 100,
    ): List<VertexInterface> {
        val adjacencyList: AdjacencyList = graph.adjacencyList()
        var speed = 1.0
        var speedEfficiency = 1.0

        val vertices = ArrayList<Vertex>()
        for (vertexIndex in 0 until graph.verticesCount()) {
            val mass: Double = 1.0 + adjacencyList.outgoingEdgesCount(vertexIndex).toDouble()
            val (x, y) = Pair(Random.nextDouble(-1.0, 1.0), Random.nextDouble(-1.0, 1.0))
            vertices.add(Vertex(mass = mass, x = x, y = y))
        }

        var outboundAttCompensation = 1.0
        if (outboundAttractionDistribution) {
            var massSum = 0.0
            vertices.forEach {
                massSum += it.mass
            }
            outboundAttCompensation = massSum / vertices.size
        }

        repeat(iterations) {
            vertices.forEach { vertex ->
                vertex.oldDx = vertex.dx
                vertex.oldDy = vertex.dy
                vertex.dx = 0.0
                vertex.dy = 0.0
            }

            if (barnesHutOptimize) Region(vertices).buildSubRegions()

            if (barnesHutOptimize) {
                Region(vertices).applyForceOnNodes(vertices, barnesHutTheta, scalingRatio)
            } else {
                utils.applyRepulsion(vertices, scalingRatio)
            }

            utils.applyGravity(vertices, gravity, scalingRatio, strongGravityMode)
            utils.applyAttraction(vertices, adjacencyList, outboundAttractionDistribution, outboundAttCompensation, edgeWeightInfluence)
            val (newSpeed, newSpeedEfficiency) = utils.adjustSpeedAndApplyForces(vertices, speed, speedEfficiency, jitterTolerance)
            speed = newSpeed
            speedEfficiency = newSpeedEfficiency
        }
        return vertices
    }
}
