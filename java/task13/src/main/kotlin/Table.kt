import java.util.concurrent.locks.ReentrantLock

class Table() {
    private val forks: ArrayList<ReentrantLock> = arrayListOf()
    private val forksLock = ReentrantLock()
    private val forksCond = forksLock.newCondition()
    init {
        for(i in 0 until 5)
            this.forks.add(ReentrantLock())
    }

    fun takeForks(num1: Int, num2: Int, name: String){
        forksLock.lock()
        try {
            while(forks[num1].isLocked || forks[num2].isLocked){
                println("$name going sleep: ${forks[num1].isLocked} || ${forks[num2].isLocked}")
                forksCond.await()
                println("$name signalled")
            }
            println("$name forks available, locking")
            takeFork(num1)
            takeFork(num2)
        } finally {
            forksLock.unlock()
        }
    }

    fun dropForks(num1: Int, num2: Int){
        forksLock.lock()
        try {
            dropFork(num1)
            dropFork(num2)
            forksCond.signalAll()
        } finally {
            forksLock.unlock()
        }
    }
    private fun takeFork(forkNum: Int){
        println("$forkNum fork Locking!")
        forks[forkNum].lock()
        println("$forkNum fork Locked!")
    }
    private fun dropFork(forkNum: Int){
        println("$forkNum fork Unlocked!")
        forks[forkNum].unlock()
    }
}