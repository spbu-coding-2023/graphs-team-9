package algorithms.layout

import kotlin.random.Random

/*
* source of realization:
* https://github.com/bhargavchippada/forceatlas2/tree/master
* GPL-3.0 license
*
*
* Temporary comment:
* Usage example with a graph represented as a 2D adjacency matrix
* fun main() {
*   val graph = arrayOf(
*       doubleArrayOf(1.0, 1.0, 1.0, 1.0),
*       doubleArrayOf(1.0, 1.0, 1.0, 1.0),
*       doubleArrayOf(1.0, 1.0, 1.0, 1.0),
*       doubleArrayOf(1.0, 1.0, 1.0, 1.0)
*   )
*   val forceAtlas2 = ForceAtlas2(verbose = true)
*   val layout: List<Pair<Double, Double>> = forceAtlas2.forceAtlas2(graph, 1000)
*   println(layout)
 */

class ForceAtlas2(
    private val outboundAttractionDistribution: Boolean = true,
    private val edgeWeightInfluence: Double = 1.0,
    private val jitterTolerance: Double = 1.0,
    private val barnesHutOptimize: Boolean = false,
    private val barnesHutTheta: Double = 1.2,
    private val scalingRatio: Double = 2.0,
    // ниже можно убрать (наверное)
    private val strongGravityMode: Boolean = false,
    private val gravity: Double = 1.0,
    private val verbose: Boolean = true,
) {
    private val utils = FA2Utils()

    private fun initVerticesAndEdges(graph: Array<DoubleArray>): Pair<List<Vertex>, List<Edge>> {
        // сюда нужно добавить проверку на симметричность матницы и на то, что это правда матрица смежности

        val vertices = mutableListOf<Vertex>()
        for (i in graph.indices) {
            val mass: Double = 1.0 + graph[i].count { it != 0.0 }
            val (x, y) = Pair(Random.nextInt().toDouble(), Random.nextInt().toDouble())
            vertices.add(Vertex(mass = mass, x = x, y = y))
        }
        val edges = mutableListOf<Edge>()
        for (sourceVertexNumber in graph.indices) {
            for (targetVertexNumber in sourceVertexNumber + 1 until graph.size) {
                if (graph[sourceVertexNumber][targetVertexNumber] != 0.0) {
                    edges.add(Edge(sourceVertexNumber, targetVertexNumber, graph[sourceVertexNumber][targetVertexNumber]))
                }
            }
        }
        return vertices to edges
    }

    fun forceAtlas2(
        graph: Array<DoubleArray>,
        iterations: Int = 100,
    ): List<Pair<Double, Double>> {
        var speed = 1.0
        var speedEfficiency = 1.0
        val (vertices, edges) = initVerticesAndEdges(graph)
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
            utils.applyAttraction(vertices, edges, outboundAttractionDistribution, outboundAttCompensation, edgeWeightInfluence)
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
