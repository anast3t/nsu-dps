import java.util.concurrent.CyclicBarrier

class Worker(
    private val department: Department,
    private val barrier: CyclicBarrier
):Thread() {
    override fun run(){
        department.performCalculations()
        println("${this.name} performed in ${department.workingSeconds}")
        barrier.await()
    }
}