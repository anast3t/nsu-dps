import java.util.concurrent.locks.ReentrantLock

class Table() {
    private val forks: ArrayList<ReentrantLock> = arrayListOf()

    init {
        for(i in 0 until 5)
            this.forks.add(ReentrantLock())
    }

    fun takeFork(forkNum: Int){
        println("$forkNum fork Locking!")
        forks[forkNum].lock()
        println("$forkNum fork Locked!")
    }
    fun dropFork(forkNum: Int){
        println("$forkNum fork Unlocked!")
        forks[forkNum].unlock()
    }
}