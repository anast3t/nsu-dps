fun main(args: Array<String>) {
    val printer = Printer()

    val second = SecondThread(printer)

    second.start()
    Thread.sleep(1000)
    for(i in 0 until 10)
//        printer.print("main","MainText $i", true, i)
        printer.printOld("main","MainText $i", true, i, i == 9)

}