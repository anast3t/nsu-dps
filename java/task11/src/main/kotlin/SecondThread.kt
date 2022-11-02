import java.util.concurrent.Semaphore

class SecondThread(
    private val childSem: Semaphore,
    private val parentSem: Semaphore,
):Thread() {
    override fun run() {
        for(i in 0 until 10){
            println("Second acquiring. Permits - ${parentSem.availablePermits()}")
            parentSem.acquire()
            println("Second message: $i")
            println("Second releasing")
            childSem.release()
        }
    }
}