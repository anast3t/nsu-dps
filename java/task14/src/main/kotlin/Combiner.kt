import java.lang.Thread.sleep
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class Combiner(
    private val produce: Component,
    private val producers: ArrayList<Producer>,
    override val maxItems: Int = 10,
): Producer() {
    override val semaphore: Semaphore = Semaphore(0) //TODO: почему не смог в абстракте в get() оставить?
    override val lock: ReentrantLock = ReentrantLock()
    override val condition: Condition = lock.newCondition()
    override fun run() {
        producers.forEach{Thread(it).start()}
        while(!Thread.currentThread().isInterrupted){
            println("${produce.name} -- Combiner gathering components")
            producers.forEach{it.passComponent()}
            println("${produce.name} -- Combiner acquired components, starting production ${semaphore.availablePermits()}")
            sleep(produce.getTime())
            println("${produce.name} -- Combiner produced")
            acceptComponent()
        }
    }
}