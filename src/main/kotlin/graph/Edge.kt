package graph

open class Edge(
    val destinationVertexIndex: Int,
    val label: String,
    val weight: Number = 1,
)
