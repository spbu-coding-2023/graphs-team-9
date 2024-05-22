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
    private val verbose: Boolean = true,
) {
    private val utils = FA2Utils()

    fun forceAtlas2(
        graph: Graph<V>,
        iterations: Int = 100,
    ): List<Pair<Double, Double>> {
        var speed = 1.0
        var speedEfficiency = 1.0

        val adjacencyList: AdjacencyList = graph.adjacencyList()
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

        val barnesHutTimer = Timer("BarnesHut Approximation")
        val repulsionTimer = Timer("Repulsion forces")
        val gravityTimer = Timer("Gravitational forces")
        val attractionTimer = Timer("Attraction forces")
        val applyForcesTimer = Timer("AdjustSpeedAndApplyForces step")

        for (i in 0 until iterations) {
            vertices.forEach { vertex ->
                vertex.oldDx = vertex.dx
                vertex.oldDy = vertex.dy
                vertex.dx = 0.0
                vertex.dy = 0.0
            }

            // Barnes Hut optimization
            if (barnesHutOptimize) {
                barnesHutTimer.start()
                val rootRegion = Region(vertices)
                rootRegion.buildSubRegions()
                barnesHutTimer.stop()
            }

            // Charge repulsion forces
            repulsionTimer.start()
            if (barnesHutOptimize) {
                val rootRegion = Region(vertices)
                rootRegion.applyForceOnNodes(vertices, barnesHutTheta, scalingRatio)
            } else {
                utils.applyRepulsion(vertices, scalingRatio)
            }
            repulsionTimer.stop()

            // Gravitational forces
            gravityTimer.start()
            utils.applyGravity(vertices, gravity, scalingRatio, strongGravityMode)
            gravityTimer.stop()

            attractionTimer.start()
            utils.applyAttraction(vertices, adjacencyList, outboundAttractionDistribution, outboundAttCompensation, edgeWeightInfluence)
            attractionTimer.stop()

            // Adjust speeds and apply forces
            applyForcesTimer.start()
            val (newSpeed, newSpeedEfficiency) = adjustSpeedAndApplyForces(vertices, speed, speedEfficiency, jitterTolerance)
            speed = newSpeed
            speedEfficiency = newSpeedEfficiency
            applyForcesTimer.stop()
        }

        if (verbose) {
            if (barnesHutOptimize) barnesHutTimer.display()
            repulsionTimer.display()
            gravityTimer.display()
            attractionTimer.display()
            applyForcesTimer.display()
        }
        return vertices.map { it.x to it.y }
    }
}
