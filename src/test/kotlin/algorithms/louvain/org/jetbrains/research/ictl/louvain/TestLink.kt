package algorithms.louvain.org.jetbrains.research.ictl.louvain

// changes:
// add:
// import org.jetbrains.research.ictl.louvain.Link

import kotlinx.serialization.Polymorphic
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.jetbrains.research.ictl.louvain.Link

@Polymorphic
@Serializable
sealed class TestLink : Link {
    abstract val source: Int
    abstract val target: Int
}

@Serializable
@SerialName("UnweightedLink")
class UnweightedLink(
    override val source: Int,
    override val target: Int,
) : TestLink() {
    override fun source() = source
    override fun target() = target
    override fun weight() = 1.0
}

@Serializable
@SerialName("WeightedLink")
class WeightedLink(
    override val source: Int,
    override val target: Int,
    val weight: Double
) : TestLink() {
    override fun source() = source
    override fun target() = target
    override fun weight() = weight
}
