class Philosopher(private val table: Table, private val position: Int): Thread() {
    var leftFork = 0
    var rightFork = 0
    override fun run() {
        leftFork = position
        rightFork = if (position+1 > 4) 0 else position+1
        while (!this.isInterrupted) {
            this.startThinking()
            this.startEating()
        }
    }
    private fun startEating(){
        println("${this.name} wants to eat")

        //So to get deadlock, you can set "swap" = true, so that all philosophers get left fork firstly
        getForks(position == 0)

        println("${this.name} is eating!")
        sleep((Math.random() * 50).toLong())

        table.dropFork(rightFork)
        sleep(50)
        table.dropFork(leftFork)
    }
    private fun startThinking(){
        val waitTime = (Math.random() * 50).toLong()
        println("${this.name} started thinking: $waitTime")
        sleep(waitTime)
    }



    private fun getForks(swap: Boolean){
        fun getLeft(){
            println("${this.name} tries to take left ($leftFork) fork")
            table.takeFork(leftFork)
        }
        fun getRight(){
            println("${this.name} tries to take right ($rightFork) fork")
            table.takeFork(rightFork)
        }
        if(!swap){
            getLeft()
            sleep(50) //Human is not ideal, give him some time
            getRight()
        } else{
            getRight()
            sleep(50)
            getLeft()
        }
    }
}