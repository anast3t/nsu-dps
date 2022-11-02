class CalcFractionThread(private val numbers: ArrayList<Int>):Thread() {
    private var sum:Double = 0.0
    fun getSum(): Double {
        return this.sum
    }
    override fun run() {
        println("${this.name} -- StartPoint: ${numbers[0]} | Endpoint: ${numbers[1]}")
        for(i in numbers[0]..numbers[1]){
            sum += calc((i).toDouble())
        }
    }
    private fun calc(n:Double): Double {
        var sign = 1
        if(n % 2 != 0.0)
            sign = -1
        return 1/(2*n+1) * sign
    }

}