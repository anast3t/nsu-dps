fun main(args: Array<String>) {
    val second = SecondThread()
    second.start()
    second.join()
    for(i in 0..10)
        println("MainText $i")

}