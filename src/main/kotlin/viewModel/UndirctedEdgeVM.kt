package viewModel

import graph.Edge

class UndirectedEdgeVM(
    source: VertexVM,
    target: VertexVM,
    edge: Edge,
) : EdgeVM(source, target, edge)
