package algorithms.layout

import kotlin.math.pow
import kotlin.math.sqrt

class Region(private val vertices: ArrayList<Vertex>) {
    private val utils = LayoutUtils
    private var mass = 0.0
    private var massCenterX = 0.0
    private var massCenterY = 0.0
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
}
