class Printer {
    private val synchronizer = Object()
    fun print(name: String, msg: String, last: Boolean){
        synchronized(synchronizer){
            synchronizer.notify()
//            println("$name going wait")
            synchronizer.wait()
            println("$name notified, printing: $msg")
            if(last){
//                println("$name last task, notifying")
                synchronizer.notify()
            }
        }
    }
}