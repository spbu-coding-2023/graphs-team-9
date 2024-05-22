package viewModel

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import graph.Graph
import java.util.AbstractMap
import java.util.TreeMap
import kotlin.random.Random

abstract class GraphVM (
    private val graph: Graph
) {
    val vertices: Array<VertexVM>
    private val vertexDefaultSize: Dp = 10.dp + (1000.dp / (graph.verticesCount() + 24))

    init {
        var i = 0
        val coordinates = graph.layout()
        val vertices = Array(graph.verticesCount()) {
            VertexVM(graph.vertexValue(i), coordinates[i].first.dp, coordinates[i++].second.dp)
        }
        this.vertices = vertices
    }

    abstract val edges: List<EdgeVM>

    private val partitionAvailabilityS = mutableStateOf(true)
    private val keyVerticesAvailabilityS = mutableStateOf(true)
    private val msfAvailabilityS = mutableStateOf(true)
    private val shortestPathAvailabilityS = mutableStateOf(true)
    private val stronglyConnectedComponentsAvailabilityS = mutableStateOf(true)
    private val cyclesAvailabilityS = mutableStateOf(true)
    private val bridgesAvailabilityS = mutableStateOf(true)

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

    private fun createNewCommunity(colors: AbstractMap<Int, Color>, communityId: Int): Color {
        val color = Color(
            Random.nextInt(25, 230),
            Random.nextInt(25, 230),
            Random.nextInt(25, 230)
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
        TODO()
    }

    fun drawMSF() {
        TODO()
    }

    fun colorShortestPath(start: VertexVM, end: VertexVM) {
        if (shortestPathAvailabilityS.value) {
            val shortestPath = graph.shortestPathByBFAlgorithm(start.data, end.data) ?: TODO()
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
                val color = Color(
                    Random.nextInt(25, 230),
                    Random.nextInt(25, 230),
                    Random.nextInt(25, 230)
                )
                component.forEach { vertices[it].color = color }

            }
        }
    }

    fun colorCycles(vertex: VertexVM) {
        if (cyclesAvailability) {
            val cycles = graph.cycles(vertex.data)
            var i = 0
            cycles.forEach { cycle -> cycle.forEach { vertices[it].pathPositions.add(i++) }; i++ }
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
        vertices.forEach { it.size = vertexDefaultSize }
    }

    fun removePaths() {
        vertices.forEach { it.pathPositions = arrayListOf() }
    }

    fun resetLayout() {
        vertices.forEach { it.removeOffset() }
    }
}
