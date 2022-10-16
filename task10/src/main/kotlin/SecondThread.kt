class SecondThread(private val printer: Printer):Thread() {
    override fun run() {
        for(i in 0 until 10){
//            printer.print(this.name,"SecondaryText $i", false, i)
            printer.printOld(this.name,"SecondaryText $i", false, i, i == 9)
        }
    }
}