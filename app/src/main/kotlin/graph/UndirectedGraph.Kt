class UndirectedGraph<V> : Graph<V>(){
        override fun addEdgeToAdjacencyList(
            firstVertexInd: Int,
            secondVertexInd: Int,
            label: String,
            weight: Int
        ){
            adjacencyList[firstVertexInd].add(Edge(secondVertexInd, label, weight))
            adjacencyList[secondVertexInd].add(Edge(firstVertexInd, label, weight))
        }
}