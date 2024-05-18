package graph

open class Edge(
    private val target: Int,
    private val label: String = "",
    private val weight: Number = 1,
) {
    fun target(): Int {
        return target
    }

    fun label(): String {
        return label
    }

    open fun weight(): Number {
        return weight
    }
}
