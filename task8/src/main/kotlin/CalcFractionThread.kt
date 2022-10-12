import java.util.Random

class CalcFractionThread(
    private val offset: Int,
    private val threadNum: Int,
    private val summarizer: SummarizerFraction,
) : Thread() {
    private var sum: Double = 0.0
    private var step: Long = 0
    fun getSum(): Double {
        return this.sum
    }

    override fun run() {
        var i: Long = offset.toLong()
        println("${this.name} Starting with offset $i and stepping $threadNum")
        fun step() {
            step++
            sum += calc((i).toDouble())
            i += threadNum
        }

        fun rangeCheck(): Boolean {
            return ((i < Long.MAX_VALUE) && (step < Long.MAX_VALUE))
        }
        while (summarizer.freeRun && rangeCheck())
            step()
        if(!rangeCheck())
            summarizer.finish()
        println("--- ${this.name} Entering await")
        summarizer.waitForThreads(step)
        println("??? ${this.name} Working up $step -- ${summarizer.maxValue}")
        while (step != summarizer.maxValue)
            step()
        println("!!! ${this.name} Worked up to $step step. N = ${i - threadNum}")
    }

    private fun calc(n: Double): Double {
        //sleep(if (Random().nextBoolean()) 5 else 0)
        var sign = 1
        if (n % 2 != 0.0)
            sign = -1
        return sign / (2 * n + 1)
    }

}