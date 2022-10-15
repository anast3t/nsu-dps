class SecondThread(private val printer: Printer):Thread() {
    override fun run() {
        for(i in 0 until 10)
            printer.print(this.name,"SecondaryText $i", i == 9)
    }
}