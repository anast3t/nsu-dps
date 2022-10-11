class SecondThread:Thread() {
    override fun run() {
        val start = System.nanoTime()
        var printedValue = start
        while(!isInterrupted){
            val newVal = (System.nanoTime() - start)/1000000
            if (newVal != printedValue){
                printedValue = newVal //Немного другой способ остановить от взрыва компьютер
                println(printedValue) 
            }
        }
        println("I'm interrupted")
    }
}
