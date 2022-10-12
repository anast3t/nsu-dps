class SummarizerFraction(private var n: Int, private val threadNum: Int) : Thread() {
    private var sum:Double = 0.0
    fun getSum(): Double {
        return this.sum
    }

    override fun run() {
        val breakpoint = n/threadNum
        for(i in 0 until threadNum){
            var startPoint = breakpoint * i
            var endPoint = startPoint + breakpoint
            if (endPoint > n || ((i == threadNum - 1) && (endPoint == (n - 1))))
                endPoint = n
            if(startPoint != 0)
                startPoint+=1

            val newThread = CalcFractionThread(
                arrayListOf(
                    startPoint,
                    endPoint
                )
            )
            newThread.start()
            this.threadStack.add(newThread)
        }

        this.threadStack.forEach {
            it.join()
            this.sum += it.getSum()
            //println("Joined: ${it.name}")
        }
        println("Summarizer ended!")
    }

    private var threadStack: ArrayList<CalcFractionThread> = arrayListOf()

}