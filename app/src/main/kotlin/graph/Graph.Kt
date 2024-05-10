abstract class Graph<V>{
    private val vertexValues : ArrayList<V> = arrayListOf()
    protected val adjacencyList : ArrayList<ArrayList<Edge>> = arrayListOf()
    private var vertexIndicesMap : HashMap<V,Int> = hashMapOf()
    private var verticesCount = 0
    private var isAbleToAdd = true

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
        if (isAbleToAdd){
            vertexIndicesMap[value] = verticesCount++
            adjacencyList.add(arrayListOf())
            vertexValues.add(value)
        }
        else{
            throw IllegalStateException("Not able to add vertices when graph is immutable")
        }
    }

    fun makeItLighterAndImmutable(){
        vertexIndicesMap = hashMapOf()
        isAbleToAdd = false
    }

    abstract fun addEdge(
        firstVertexValue: V,
        secondVertexValue: V,
        label : String = "",
        weight: Int = 1
    )

}
