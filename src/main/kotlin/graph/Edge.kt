package graph

open class Edge(
    private val target: Int,
    private val label: String = "",
    private val weight: Double = 1.0,
) {
    fun target(): Int {
        return target
    }

    fun label(): String {
        return label
    }

    open fun weight(): Double {
        return weight
    }
}
