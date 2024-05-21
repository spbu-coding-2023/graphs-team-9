package algorithms.layout

class Timer(private val name: String = "Timer") {
    private var startTime: Long = 0
    private var totalTime: Long = 0

    fun start() {
        startTime = System.currentTimeMillis()
    }

    fun stop() {
        totalTime += (System.currentTimeMillis() - startTime)
    }

    fun display() {
        println("$name took ${"%.2f".format(totalTime / 1000.0)} seconds")
    }
}
