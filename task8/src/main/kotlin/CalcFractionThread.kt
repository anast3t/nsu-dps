import java.util.concurrent.CyclicBarrier

class CalcFractionThread(
    private val offset:Int,
    private val threadNum:Int,
    private val summarizer:SummarizerFraction,
    ):Thread() {
    private var sum:Double = 0.0
    private var step:Int = 0
    fun getSum(): Double {
        return this.sum
    }
    override fun run() {
        var i = offset
        println("${this.name} Starting with offset $i and stepping $threadNum")
        fun step(){
            step++
            sum += calc((i).toDouble())
            i+=threadNum
        }
        while(summarizer.freeRun)
            step()

        println("--- ${this.name} Entering await")
        summarizer.waitForThreads(step)
        println("??? ${this.name} Working up $step -- ${summarizer.maxValue}")
        while(step != summarizer.maxValue)
            step()
        println("!!! ${this.name} Worked up to $step step. N = ${i-threadNum}")
    }
    private fun calc(n:Double): Double {
        var sign = 1
        if(n % 2 != 0.0)
            sign = -1
        //sleep(100)
        //println("$n -- $sign")
        return sign/(2*n+1)
    }

}