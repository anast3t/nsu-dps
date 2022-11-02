import java.util.concurrent.Semaphore

fun main(args: Array<String>) {
    val parentSem = Semaphore(0)
    val childSem = Semaphore(0)
    val thread = SecondThread(childSem, parentSem)
    thread.start()
    for(i in 0 until 10){
        println("Main message: $i")
        println("Main releasing")
        parentSem.release()
        Thread.sleep(1000)
        println("Main acquiring. Permits - ${childSem.availablePermits()}") //Can't deadlock, because permits stack
        childSem.acquire()
    }
    thread.join()
}