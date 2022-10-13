import java.util.Objects
import java.util.concurrent.CyclicBarrier

class SummarizerFraction(private val threadNum: Int) : Thread() {
    private var sum: Double = 0.0
    fun getSum(): Double {
        return this.sum
    }

    @Volatile
    var freeRun: Boolean = true

    @Volatile
    var maxValue: Long = 0L

    private val valueWriteLock = Object()

    private var threadStack: ArrayList<CalcFractionThread> = arrayListOf()
    private val barrier = CyclicBarrier(threadNum)
    override fun run() {
        for (i in 0 until threadNum) {
            val newThread = CalcFractionThread(i, threadNum, this)
            newThread.start()
            this.threadStack.add(newThread)
        }

        this.threadStack.forEach {
            it.join()
            this.sum += it.getSum()
            println("Joined: ${it.name}")
        }
        println("Summarizer ended!")
    }

    fun waitForThreads(value: Long): Long {
        synchronized(valueWriteLock){
            maxValue = if (value > maxValue) value else maxValue
        }
        barrier.await()
        return maxValue
    }

    fun finish() {
        println("Finishing")
        this.freeRun = false
    }

}