class CalcPartThread(
    private val n:Int,
    private val summarizer: Summarizer,
    private val solo:Boolean
    ):Thread() {

    override fun run() {
        //println("Starting: ${this.name}")
        summarizer.pushToNumStack(calc(n.toDouble(), solo), this.name)
    }

    private fun calc(n:Double, solo:Boolean): Double {
        sleep(2) //TODO: Эта затычка чтоб показать, что тредпул реально работает...
        //println("${this.name} calculating on: $n")
        val firstPart =  1/(2*n+1)
        if(solo){
            return firstPart
        }
        val secondPart = 1/(2*(n+1)+1)
        return firstPart - secondPart
    }
}