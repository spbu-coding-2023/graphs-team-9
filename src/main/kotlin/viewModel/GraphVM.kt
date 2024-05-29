package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graph.Graph
import java.util.AbstractMap
import java.util.TreeMap
import kotlin.random.Random

abstract class GraphVM(
    private val graph: Graph,
) {
    private val standardWidth = 1024
    private val standardHeight = 736
    private val widthState = mutableStateOf(standardWidth)
    private val heightState = mutableStateOf(standardHeight)
    private val vertexDefaultSize = mutableStateOf(10.dp + (1000.dp / (graph.verticesCount() + 24)))
    var vertices = Array(graph.verticesCount()) { i -> VertexVM(graph.vertexValue(i), 0.dp, 0.dp, vertexDefaultSize.value) }
    private val unscaledCoordinates = graph.layoutGraph()
    abstract val edges: List<EdgeVM>
    var height: Int
        get() = heightState.value
        set(height) {
            heightState.value = height
        }

    var width: Int
        get() = widthState.value
        set(width) {
            widthState.value = width
        }

    private val partitionAvailabilityS = mutableStateOf(true)
    private val keyVerticesAvailabilityS = mutableStateOf(true)
    private val msfAvailabilityS = mutableStateOf(true)
    private val shortestPathAvailabilityS = mutableStateOf(true)
    private val stronglyConnectedComponentsAvailabilityS = mutableStateOf(true)
    private val cyclesAvailabilityS = mutableStateOf(true)
    private val bridgesAvailabilityS = mutableStateOf(true)

    private fun makeCoordinatesScaled(
        unscaledVertices: List<Pair<Double, Double>>,
        width: Int,
        height: Int,
        radius: Int,
    ): List<Pair<Double, Double>> {
        val leftOffset = radius / 2
        val rightOffset = width / 10
        val topOffset = radius / 2
        val bottomOffset = radius / 2
        val padding = radius * 2
        val minX = unscaledVertices.minOf { it.first }
        val maxX = unscaledVertices.maxOf { it.first }
        val minY = unscaledVertices.minOf { it.second }
        val maxY = unscaledVertices.maxOf { it.second }
        val scaleX = width - padding - leftOffset - rightOffset
        val scaleY = height - padding - topOffset - bottomOffset
        val newCoordinates =
            unscaledVertices.map { point ->
                ((point.first - minX) / (maxX - minX)) * scaleX + radius + leftOffset to ((point.second - minY) / (maxY - minY)) * scaleY + topOffset
            }

        val offsetX = (width - leftOffset - rightOffset - (newCoordinates.maxOf { it.first } - newCoordinates.minOf { it.first })) / 2
        val offsetY = (height - topOffset - bottomOffset - (newCoordinates.maxOf { it.second } - newCoordinates.minOf { it.second })) / 2
        return newCoordinates.map { point ->
            point.first + offsetX to point.second + offsetY
        }
    }

    private fun makeCoordinatesUnscaled(
        unscaledVertices: List<Pair<Double, Double>>,
        width: Int,
        height: Int,
        radius: Int,
    ): List<Pair<Double, Double>> {
        TODO()
    }


    var partitionAvailability: Boolean
        get() = partitionAvailabilityS.value
        set(availability) {
            partitionAvailabilityS.value = availability
        }

    var keyVerticesAvailability: Boolean
        get() = keyVerticesAvailabilityS.value
        set(availability) {
            keyVerticesAvailabilityS.value = availability
        }

    var mfsAvailability: Boolean
        get() = msfAvailabilityS.value
        set(availability) {
            msfAvailabilityS.value = availability
        }

    var shortestPathAvailability: Boolean
        get() = shortestPathAvailabilityS.value
        set(availability) {
            shortestPathAvailabilityS.value = availability
        }

    var stronglyConnectedComponentsAvailability: Boolean
        get() = stronglyConnectedComponentsAvailabilityS.value
        set(availability) {
            stronglyConnectedComponentsAvailabilityS.value = availability
        }

    var cyclesAvailability: Boolean
        get() = cyclesAvailabilityS.value
        set(availability) {
            cyclesAvailabilityS.value = availability
        }

    var bridgesAvailability: Boolean
        get() = bridgesAvailabilityS.value
        set(availability) {
            bridgesAvailabilityS.value = availability
        }

    private fun createNewCommunity(
        colors: AbstractMap<Int, Color>,
        communityId: Int,
    ): Color {
        val color =
            Color(
                Random.nextInt(25, 230),
                Random.nextInt(25, 230),
                Random.nextInt(25, 230),
            )
        colors[communityId] = color
        return color
    }

    fun colorCommunities() {
        if (partitionAvailabilityS.value) {
            val partition = graph.partition()
            val colors = TreeMap<Int, Color>()
            for ((vertex, communityId) in partition) {
                vertices[vertex].color = colors[communityId] ?: createNewCommunity(colors, communityId)
            }
        }
    }

    fun changeVerticesSizes() {
        if (keyVerticesAvailabilityS.value) {
            val ratios = graph.keyVertices()
            vertices.forEachIndexed { i, vertex -> vertex.size *= (2 * ratios[i]).toFloat() }
        }
    }

    fun colorMSFEdges() {
        if (msfAvailabilityS.value) {
            val msf = graph.minimumSpanningForest()
            var i = 0
            msf.svsEdgesList().forEach {
                vertices[it.source()].pathPositions.add(i)
                vertices[it.target()].pathPositions.add(++i)
                i += 2
            }
        }
    }

    fun colorShortestPath(
        start: String,
        end: String,
    ) {
        if (shortestPathAvailabilityS.value) {
            val shortestPath = graph.shortestPathByBFAlgorithm(start, end) ?: TODO()
            for ((i, vertex) in shortestPath.withIndex()) {
                vertices[vertex].pathPositions.add(i)
            }
        }
    }

    fun colorStronglyConnectedComponents() {
        if (stronglyConnectedComponentsAvailabilityS.value) {
            val components = graph.stronglyConnectedComponents()
            if (components.size == 0) {
                TODO()
            }
            components.forEach { component ->
                val color =
                    Color(
                        Random.nextInt(25, 230),
                        Random.nextInt(25, 230),
                        Random.nextInt(25, 230),
                    )
                component.forEach { vertices[it].color = color }
            }
        }
    }

    fun colorCycles(vertex: String) {
        if (cyclesAvailability) {
            val cycles = graph.cycles(vertex)
            var i = 0
            cycles.forEach { cycle ->
                cycle.forEach { vertices[it].pathPositions.add(i++) }
                i++
            }
        }
    }

    fun colorBridges() {
        if (bridgesAvailability) {
            val bridges = graph.findBridges()
            var i = 0
            bridges.forEach {
                vertices[it.first()].pathPositions.add(i)
                vertices[it.last()].pathPositions.add(++i)
                i += 2
            }
        }
    }

    fun resetVertexColors() {
        vertices.forEach { it.color = Color.White }
    }

    fun resetSizes() {
        vertices.forEach { it.size = vertexDefaultSize.value }
    }

    fun removePaths() {
        vertices.forEach { it.pathPositions = arrayListOf() }
    }

    fun resetLayout() {
        vertices.forEach { it.removeOffset() }
    }

    fun layout() {
        val newVertexDefaultSize: Dp =
            10.dp + (1000.dp * (heightState.value * widthState.value) / (1024 * 736) / (graph.verticesCount() + 24))
        val scaledCoordinates =
            makeCoordinatesScaled(unscaledCoordinates, widthState.value, heightState.value, newVertexDefaultSize.value.toInt())
        vertices.forEachIndexed { i, vertex ->
            vertex.x = scaledCoordinates[i].first.dp
            vertex.y = scaledCoordinates[i].second.dp
            vertex.size *= newVertexDefaultSize / vertexDefaultSize.value
        }
        vertexDefaultSize.value = newVertexDefaultSize
    }
}
