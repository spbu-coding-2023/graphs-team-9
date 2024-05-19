package algorithms

import graph.SourceVertexStoringEdge
import kotlin.collections.ArrayList

class BoruvkaSAlgorithm(private val edges: List<SourceVertexStoringEdge>, private val verticesCount: Int) {
    private val minimumSpanningForestSEdges: ArrayList<SourceVertexStoringEdge> = arrayListOf()
    private val connectedComponents = UnionFind(verticesCount)
    private lateinit var cheapestComponentSEdges: Array<SourceVertexStoringEdge?>
    private lateinit var cheapestEdgeSIndices: IntArray

    fun boruvkaSAlgo(): ArrayList<SourceVertexStoringEdge> {
        var didAlgorithmSIterateMakeChanges: Boolean
        var isItFirstIteration = true
        while (true) {
            cheapestComponentSEdges = Array(verticesCount) { null }
            cheapestEdgeSIndices = IntArray(verticesCount) { -1 }
            didAlgorithmSIterateMakeChanges = false
            for ((edgeInd, edge) in edges.withIndex()) {
                val source = edge.source()
                val target = edge.target()
                if (isItFirstIteration) {
                    if (!(source in 0 until verticesCount && target in 0 until verticesCount)) {
                        throw IllegalArgumentException(
                            "Vertex's index (${if (source in 0 until verticesCount) target else source})" +
                                " >= vertices count ($verticesCount)",
                        )
                    }
                    isItFirstIteration = false
                }
                val sourcesSConnectedComponent: Int = connectedComponents.find(source)
                val targetSConnectedComponent: Int = connectedComponents.find(target)
                if (sourcesSConnectedComponent != targetSConnectedComponent) {
                    if (compareWithCheapest(edge, edgeInd, sourcesSConnectedComponent) < 0) {
                        updateCheapest(edge, edgeInd, sourcesSConnectedComponent)
                        didAlgorithmSIterateMakeChanges = true
                    }
                    if (compareWithCheapest(edge, edgeInd, targetSConnectedComponent) < 0) {
                        updateCheapest(edge, edgeInd, targetSConnectedComponent)
                        didAlgorithmSIterateMakeChanges = true
                    }
                }
            }
            if (!didAlgorithmSIterateMakeChanges) {
                break
            }
            for (edge in setOf(*cheapestComponentSEdges)) {
                edge?.let {
                    minimumSpanningForestSEdges.add(it)
                    connectedComponents.unite(it.source(), it.target())
                }
            }
        }
        return minimumSpanningForestSEdges
    }

    private fun compareWithCheapest(
        edge: SourceVertexStoringEdge,
        edgeInd: Int,
        component: Int,
    ): Int {
        val currentCheapest = cheapestComponentSEdges[component] ?: return -Int.MAX_VALUE
        val weightComparison = edge.weight().compareTo(currentCheapest.weight())
        if (weightComparison == 0) {
            return edgeInd.compareTo(cheapestEdgeSIndices[component])
        }
        return weightComparison
    }

    private fun updateCheapest(
        edge: SourceVertexStoringEdge,
        edgeInd: Int,
        component: Int,
    ) {
        cheapestComponentSEdges[component] = edge
        cheapestEdgeSIndices[component] = edgeInd
    }
}

internal class UnionFind(membersCount: Int) {
    private val roots = IntArray(membersCount) { member -> member }

    fun find(member: Int): Int {
        val root = roots[member]
        if (root != member && root != roots[root]) {
            var curMember = root
            var curMemberSRoot = roots[root]
            val path = arrayListOf(member)
            while (curMember != curMemberSRoot) {
                path.add(curMember)
                curMember = curMemberSRoot
                curMemberSRoot = roots[curMember]
            }
            for (pathMember in path) {
                roots[pathMember] = curMember
            }
        }
        return roots[member]
    }

    fun unite(
        firstMember: Int,
        secondMember: Int,
    ) {
        roots[find(firstMember)] = roots[find(secondMember)]
    }
}
