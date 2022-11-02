import java.util.concurrent.locks.ReentrantLock

fun main(args: Array<String>) {
    val table = Table()
    val philosophers: ArrayList<Philosopher> = arrayListOf()
    for(i in 0 until 5){
        philosophers.add(Philosopher(table, i))
        philosophers[i].start()
    }
    philosophers.forEach { it.join() }
}
