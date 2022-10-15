import java.util.concurrent.CyclicBarrier

class Founder(company: Company) {
    private val workers: ArrayList<Worker> = arrayListOf()
    private val barrier: CyclicBarrier

    init {
        barrier = CyclicBarrier(company.getDepartmentsCount()){company.showCollaborativeResult()}
        for(i in 0 until company.getDepartmentsCount())
            workers.add(Worker(company.getFreeDepartment(i), barrier))
    }

    fun start() {
        workers.forEach { it.start() }
    }
}