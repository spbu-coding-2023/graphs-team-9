abstract class Graph<V>{
        val vertexValues : ArrayList<V> = arrayListOf()
    
        abstract fun getAdjacencyList() : ArrayList<ArrayList<Edge>>
    }
    