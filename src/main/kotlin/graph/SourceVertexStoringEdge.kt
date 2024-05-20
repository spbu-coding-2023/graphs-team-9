package graph

import org.jetbrains.research.ictl.louvain.Link

open class SourceVertexStoringEdge(
    private val source: Int,
    target: Int,
    label: String = "",
    weight: Double = 1.0,
) : Edge(target, label, weight), Link {
    override fun source(): Int {
        return source
    }

    override fun weight(): Double {
        return super.weight().toDouble()
    }
}
