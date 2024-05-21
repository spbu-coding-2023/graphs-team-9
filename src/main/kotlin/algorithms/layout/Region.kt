package algorithms.layout

import kotlin.math.max
import kotlin.math.min
import kotlin.math.pow
import kotlin.math.sqrt

class Region(private val vertices: List<Vertex>) {
    private val utils = FA2Utils()
    var mass = 0.0
    var massCenterX = 0.0
    var massCenterY = 0.0
    private var size = 0.0
    private var subregions: MutableList<Region> = mutableListOf()

    init {
        updateMassAndGeometry()
    }

    private fun updateMassAndGeometry() {
        if (vertices.size > 1) {
            mass = 0.0
            var massSumX = 0.0
            var massSumY = 0.0
            for (vertex in vertices) {
                mass += vertex.mass
                massSumX += vertex.x * vertex.mass
                massSumY += vertex.y * vertex.mass
            }
            massCenterX = massSumX / mass
            massCenterY = massSumY / mass

            size = 0.0
            for (vertex in vertices) {
                val distance = sqrt((vertex.x - massCenterX).pow(2) + (vertex.y - massCenterY).pow(2))
                size = maxOf(size, 2 * distance)
            }
        }
    }

    fun buildSubRegions() {
        if (vertices.size > 1) {
            val topLeftVertices = mutableListOf<Vertex>()
            val bottomLeftVertices = mutableListOf<Vertex>()
            val topRightVertices = mutableListOf<Vertex>()
            val bottomRightVertices = mutableListOf<Vertex>()

            for (vertex in vertices) {
                if (vertex.x < massCenterX) {
                    if (vertex.y < massCenterY) {
                        bottomLeftVertices.add(vertex)
                    } else {
                        topLeftVertices.add(vertex)
                    }
                } else {
                    if (vertex.y < massCenterY) {
                        bottomRightVertices.add(vertex)
                    } else {
                        topRightVertices.add(vertex)
                    }
                }
            }

            listOf(topLeftVertices, bottomLeftVertices, topRightVertices, bottomRightVertices).forEach { regionVertices ->
                if (regionVertices.isNotEmpty()) {
                    if (regionVertices.size < vertices.size) {
                        subregions.add(Region(regionVertices))
                    } else {
                        regionVertices.forEach {
                            subregions.add(Region(listOf(it)))
                        }
                    }
                }
            }
            subregions.forEach { it.buildSubRegions() }
        }
    }

    private fun applyForce(
        vertex: Vertex,
        theta: Double,
        coefficient: Double = 0.0,
    ) {
        if (vertices.size < 2) {
            utils.linRepulsion(vertex, vertices[0], coefficient)
        } else {
            val distance = sqrt((vertex.x - massCenterX).pow(2) + (vertex.y - massCenterY).pow(2))
            if (distance * theta > size) {
                utils.linRepulsionRegion(vertex, this, coefficient)
            } else {
                subregions.forEach { it.applyForce(vertex, theta, coefficient) }
            }
        }
    }

    fun applyForceOnNodes(
        vertices: List<Vertex>,
        theta: Double,
        coefficient: Double = 0.0,
    ) {
        vertices.forEach { applyForce(it, theta, coefficient) }
    }
}

fun adjustSpeedAndApplyForces(
    vertices: List<Vertex>,
    speed: Double,
    speedEfficiency: Double,
    jitterTolerance: Double,
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
        jitterTolerance *
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
    if (totalEffectiveTraction > 2.0 && totalSwinging / totalEffectiveTraction > 2.0) {
        if (newSpeedEfficiency > minSpeedEfficiency) {
            newSpeedEfficiency *= .5
        }
    }

    val targetSpeed =
        if (totalSwinging == 0.0) {
            Double.MAX_VALUE
        } else {
            jt * newSpeedEfficiency * totalEffectiveTraction / totalSwinging
        }

    if (totalSwinging > jt * totalEffectiveTraction) {
        if (newSpeedEfficiency > minSpeedEfficiency) {
            newSpeedEfficiency *= .7
        }
    } else if (speed < 1000) {
        newSpeedEfficiency *= 1.3
    }

    val maxRise = .5
    val newSpeed = speed + min(targetSpeed - speed, maxRise * speed)

    vertices.forEach {
        val swinging = it.mass * sqrt((it.oldDx - it.dx).pow(2) + (it.oldDy - it.dy).pow(2))
        val factor = speed / (1.0 + sqrt(speed * swinging))
        it.x += (it.dx * factor)
        it.y += (it.dy * factor)
    }

    return newSpeed to minSpeedEfficiency
}
