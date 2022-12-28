class Philosopher(private val table: Table, private val position: Int): Thread() {
    private var leftFork = 0
    private var rightFork = 0
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

        getForks()

        println("${this.name} is eating!")
        sleep((Math.random() * 50).toLong())
        println("$name ate, dropping forks ($leftFork, $rightFork)")
        table.dropForks(leftFork, rightFork)
    }
    private fun startThinking(){
        val waitTime = (Math.random() * 50).toLong()
        println("${this.name} started thinking: $waitTime")
        sleep(waitTime)
    }

    private fun getForks(){
        println("$name tries to take ($leftFork, $rightFork) forks")
        table.takeForks(leftFork, rightFork, name)
    }
}