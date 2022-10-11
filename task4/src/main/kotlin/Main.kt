import java.util.*
import kotlin.concurrent.timerTask

fun main(args: Array<String>) {
    var thread: SecondThread? = null;
    val timer = Timer()
    timer.schedule(timerTask {
        thread = SecondThread()
        thread!!.start()
    }, 0)
    timer.schedule(timerTask{
        thread!!.interrupt()
        timer.cancel()
    }, 2000)
}