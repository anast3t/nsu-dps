class SecondThread:Thread() {
    override fun run() {
        val start = System.nanoTime()
        while(true){ //ATTENTION: конструкция sleep/try/catch чтоб компьютер не взорвался от вывода на голом цикле.
            println((System.nanoTime() - start)/1000000)
            try{
                sleep(100)
            }catch(e: InterruptedException){

                println("I was killed!") //Не для условия пятой
                return
            }
        }
    }
}