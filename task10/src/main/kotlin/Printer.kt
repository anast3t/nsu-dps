class Printer {
    private val synchronizer = Object()
    private var safetyFlag:Boolean = false
    fun print(name: String, msg: String, safetyFlagStep: Boolean, loopStep: Int){
        println("$loopStep--$name Entered part BEFORE synchronizer")
        synchronized(synchronizer){
            println("$loopStep--$name Entered part AFTER synchronizer")
            if(safetyFlag == safetyFlagStep){
                println("$loopStep--$name going wait: $safetyFlag === $safetyFlagStep")
                while (safetyFlag == safetyFlagStep){
                    synchronizer.wait()
                }
            } else println("$loopStep--$name-s step, not waiting: $safetyFlag !== $safetyFlagStep")
            println("$loopStep--$name notified, printing: $msg")
            println("$loopStep--$name changING $safetyFlag -> $safetyFlagStep")
            safetyFlag = safetyFlagStep
            println("$loopStep--$name changED. Curr: $safetyFlag")
            synchronizer.notifyAll()
            println("$loopStep--$name called notifyAll()")
        }
    }
}