package algorithms.layout

import kotlin.math.pow
import kotlin.math.sqrt

class Region(private val vertices: ArrayList<Vertex>) {
    private val utils = LayoutUtils
    var mass = 0.0
    var massCenterX = 0.0
    var massCenterY = 0.0
    private var size = 0.0
    private var subregions = ArrayList<Region>()

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
            val topLeftVertices = arrayListOf<Vertex>()
            val bottomLeftVertices = arrayListOf<Vertex>()
            val topRightVertices = arrayListOf<Vertex>()
            val bottomRightVertices = arrayListOf<Vertex>()

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

            arrayListOf(topLeftVertices, bottomLeftVertices, topRightVertices, bottomRightVertices).forEach { regionVertices ->
                if (regionVertices.isNotEmpty()) {
                    if (regionVertices.size < vertices.size) {
                        subregions.add(Region(regionVertices))
                    } else {
                        regionVertices.forEach {
                            subregions.add(Region(arrayListOf(it)))
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
        vertices: ArrayList<Vertex>,
        theta: Double,
        coefficient: Double = 0.0,
    ) {
        vertices.forEach { applyForce(it, theta, coefficient) }
    }
}
