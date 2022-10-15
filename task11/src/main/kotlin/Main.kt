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
        println("Main acquiring")
        childSem.acquire()
    }
    thread.join()
}