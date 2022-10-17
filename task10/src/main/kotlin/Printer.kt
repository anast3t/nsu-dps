class Printer {
    private val synchronizer = Object()
    private var safetyFlag:Boolean = false

    private fun notificationPart(loopStep: Int, name: String, safetyFlagStep: Boolean){
        println("$loopStep--$name changING $safetyFlag -> $safetyFlagStep")
        safetyFlag = safetyFlagStep
        println("$loopStep--$name changED. Curr: $safetyFlag")
        synchronizer.notifyAll()
        println("$loopStep--$name called notifyAll()")
    }

    private fun sleepPart(loopStep: Int, name: String, safetyFlagStep: Boolean){
        if(safetyFlag == safetyFlagStep){
            println("$loopStep--$name going wait: $safetyFlag === $safetyFlagStep")
            while (safetyFlag == safetyFlagStep)
                synchronizer.wait()
        } else println("$loopStep--$name-s step, not waiting: $safetyFlag !== $safetyFlagStep")
    }
    fun print(name: String, msg: String, safetyFlagStep: Boolean, loopStep: Int){
        /*Здесь получается неприятное для задачи поведение -
          треды почти никогда не уходят в сон, по факту работая на синхронайзере
          Пример добавил в output.txt, в той же папке с кодом
        */

        println("$loopStep--$name Entered part BEFORE synchronizer")
        synchronized(synchronizer){
            println("$loopStep--$name Entered part AFTER synchronizer")
            sleepPart(loopStep, name, safetyFlagStep)
            println("$loopStep--$name notified, printing: $msg")
            notificationPart(loopStep, name, safetyFlagStep)
        }
    }

    fun printOld(name: String, msg: String, safetyFlagStep: Boolean, loopStep: Int, last: Boolean){

        /*А вот здесь как изначально и задумывалось, по очереди просыпаются и засыпают*/

        println("$loopStep--$name Entered part BEFORE synchronizer")
        synchronized(synchronizer){
            println("$loopStep--$name Entered part AFTER synchronizer")
            notificationPart(loopStep, name, safetyFlagStep)
            sleepPart(loopStep, name, safetyFlagStep)
            println("$loopStep--$name notified, printing: $msg")
            if(last){
                println("$loopStep--$name last task, notifying")
                notificationPart(loopStep, name, safetyFlagStep)
            }
        }
    }
    

}