class SecondThread:Thread() {
    override fun run() {
        for(i in 0..10)
            println("SecondaryText $i")
    }
}