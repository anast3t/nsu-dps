import java.util.concurrent.Semaphore
import java.util.concurrent.locks.Condition
import java.util.concurrent.locks.ReentrantLock

abstract class Producer: Runnable {
    open val maxItems: Int
        get() = 10
    abstract val semaphore: Semaphore
    abstract val lock : ReentrantLock
    abstract val condition : Condition

    fun acceptComponent() {
        lock.lock()
        try {
            if (semaphore.availablePermits() >= maxItems) {
                while (semaphore.availablePermits() >= maxItems) {
                    condition.await()
                }
            }
            semaphore.release()
        } finally {
            lock.unlock()
        }
    }

    fun passComponent() {
        semaphore.acquire()
        lock.lock()
        try {
            condition.signalAll()
        } finally {
            lock.unlock()
        }
    }

    abstract override fun run()
}