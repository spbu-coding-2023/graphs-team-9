package algorithms

import graph.SourceVertexStoringEdge

internal class ConnectedComponent(
    val id: Int,
    var cheapestEdge: SourceVertexStoringEdge? = null,
    var cheapestEdgeInd: Int = -1,
)

class BoruvkaSAlgorithm(private val edges: List<SourceVertexStoringEdge>, private val verticesCount: Int) {
    private val minimumSpanningForestSEdges: MutableList<SourceVertexStoringEdge> = mutableListOf()
    private var didAlgorithmSIterateMakeChanges = true

    fun boruvkaSAlgo(): List<SourceVertexStoringEdge> {
        var isItFirstIteration = true
        while (true) {
            val connectedComponentAffiliations = Array(verticesCount) { vertex -> ConnectedComponent(vertex) }
            didAlgorithmSIterateMakeChanges = false
            for (edge in minimumSpanningForestSEdges) {
                val sourcesSConnectedComponent = connectedComponentAffiliations[edge.source()]
                val targetSConnectedComponent = connectedComponentAffiliations[edge.target()]
                if (sourcesSConnectedComponent !== targetSConnectedComponent) {
                    if (sourcesSConnectedComponent.id < targetSConnectedComponent.id) {
                        connectedComponentAffiliations[edge.target()] = sourcesSConnectedComponent
                    } else {
                        connectedComponentAffiliations[edge.source()] = targetSConnectedComponent
                    }
                }
            }
            for ((edgeInd, edge) in edges.withIndex()) {
                if (isItFirstIteration) {
                    val source = edge.source()
                    val target = edge.target()
                    if (!(source in 0 until verticesCount && target in 0 until verticesCount)) {
                        throw IllegalArgumentException(
                            "Vertex's index (${if (source in 0 until verticesCount) target else source})" +
                                " >= vertices count ($verticesCount)",
                        )
                    }
                }
                val sourcesSConnectedComponent = connectedComponentAffiliations[edge.source()]
                val targetSConnectedComponent = connectedComponentAffiliations[edge.target()]
                if (sourcesSConnectedComponent !== targetSConnectedComponent) {
                    if (compareWithCheapest(edge, edgeInd, sourcesSConnectedComponent) < 0) {
                        updateCheapest(edge, edgeInd, sourcesSConnectedComponent)
                    }
                    if (compareWithCheapest(edge, edgeInd, targetSConnectedComponent) < 0) {
                        updateCheapest(edge, edgeInd, targetSConnectedComponent)
                    }
                }
            }
            val cheapestEdges = setOf(*Array(verticesCount) { vertex -> connectedComponentAffiliations[vertex].cheapestEdge })
            if (!didAlgorithmSIterateMakeChanges) {
                break
            }
            for (edge in cheapestEdges) {
                edge?.let { minimumSpanningForestSEdges.add(it) }
            }
            isItFirstIteration = false
        }
        return minimumSpanningForestSEdges.toList()
    }

    private fun compareWithCheapest(
        edge: SourceVertexStoringEdge,
        edgeInd: Int,
        component: ConnectedComponent,
    ): Int {
        val currentCheapest = component.cheapestEdge ?: return -Int.MAX_VALUE
        val weightComparison = edge.weight().compareTo(currentCheapest.weight())
        if (weightComparison == 0) {
            return edgeInd.compareTo(component.cheapestEdgeInd)
        }
        return weightComparison
    }

    private fun updateCheapest(
        edge: SourceVertexStoringEdge,
        edgeInd: Int,
        component: ConnectedComponent,
    ) {
        component.cheapestEdge = edge
        component.cheapestEdgeInd = edgeInd
        didAlgorithmSIterateMakeChanges = true
    }
}
