fun main(args: Array<String>) {
    val thread = SecondThread()
    thread.start()
    thread.join(2000)
    if (thread.isAlive)
        thread.interrupt()
}