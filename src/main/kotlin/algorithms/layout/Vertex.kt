package algorithms.layout

class Vertex(
    var mass: Double = 0.0,
    var oldDx: Double = 0.0,
    var oldDy: Double = 0.0,
    var dx: Double = 0.0,
    var dy: Double = 0.0,
    override var x: Double,
    override var y: Double,
) : VertexInterface
