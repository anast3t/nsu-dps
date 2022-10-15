fun main(args: Array<String>) {
    val printer = Printer()

    val second = SecondThread(printer)

    second.start()
    for(i in 0 until 10)
        printer.print("main","MainText $i", i == 9)

}