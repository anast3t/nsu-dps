import java.util.concurrent.Executors

class Summarizer(private var n: Int, private val threadNum: Int) : Thread() {
    private var sum:Double = 0.0
    fun getSum(): Double {
        return this.sum
    }

    override fun run() {
        //TODO: Надо признать, что время на обработку тредпула уходит несравнимо больше чем на сам тред
        val executor = Executors.newFixedThreadPool(threadNum)

        if(n % 2 != 0){
            executor.execute(CalcPartThread(n-1, this, false))
            n -= 1
        }
        n /= 2
        for(i in 0 until n){
            executor.execute(CalcPartThread(i*2, this, false))
        }
        executor.shutdown()
        while(!executor.isTerminated){
            sleep(100)
        }
        println("Summarizer ended!")
    }

    @Synchronized fun pushToNumStack(num: Double) {
        this.sum += num
    }


}