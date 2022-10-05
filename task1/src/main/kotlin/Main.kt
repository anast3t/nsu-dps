fun main(args: Array<String>) {
    val second = SecondThread()
    second.start()
    for(i in 0..10)
        println("MainText $i")

}