abstract class Graph<V>{
    private val vertexValues : ArrayList<V> = arrayListOf()
    private val adjacencyList : ArrayList<ArrayList<Edge>> = arrayListOf()
    private var verticesCount = 0
    private var vertexIndicesMap : HashMap<V,Int> = hashMapOf()
    
    fun getAdjacencyList() : ArrayList<ArrayList<Edge>>{
        return adjacencyList
    }
        
    fun getVertexValues() : ArrayList<V>{
        return vertexValues
    }

    fun getVerticesCount() : Int{
        return verticesCount
    }

    fun addVertex(value : V){
        vertexIndicesMap[value] = verticesCount++
        vertexValues.add(value)
    }
}
