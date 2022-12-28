import java.util.Timer
import kotlin.concurrent.timerTask

fun main() {
    val list = MySLList()
    val interval = Timer()

    interval.schedule(timerTask { list.sort() }, 5000, 5000)

    while (true) {
        val input = readln()
        if (input.isEmpty()) {
            list.print()
        } else {
            list.push(input)
        }
    }
}