import java.lang.Thread.sleep
import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

class Fabric (
    private val produce: Component,
    override val maxItems: Int = 10,
): Producer() {
    override val semaphore: Semaphore = Semaphore(0)
    override val lock: ReentrantLock = ReentrantLock()
    override val condition: Condition = lock.newCondition()
    override fun run (){
        while(!Thread.currentThread().isInterrupted) {
            println("${produce.name} -- Fabric starting produce ${semaphore.availablePermits()}")
            sleep(produce.getTime())
            println("${produce.name} -- Fabric produced")
            acceptComponent()
        }
    }
}