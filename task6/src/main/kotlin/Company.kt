import java.util.function.BinaryOperator

class Company(departmentsCount: Int) {
    private val departments: List<Department>

    init {
        departments = ArrayList<Department>(departmentsCount)
        for (i in 0 until departmentsCount) {
            departments.add(i, Department(i))
        }
    }

    /**
     * Вывод результата по всем отделам.
     * P.S. Актуально после того, как все отделы выполнят свою работу.
     */
    fun showCollaborativeResult() {
        println("All departments have completed their work.")
        val result: Int = departments.map(Department::calculationResult).reduce { x, y -> x + y }
        println("The sum of all calculations is: $result")
    }

    /**
     * @return Количество доступных отделов для симуляции выполнения
     * работы.
     */
    fun getDepartmentsCount(): Int {
        return departments.size
    }

    /**
     * @param index Индекс для текущего свободного отдела.
     * @return Свободный отдел для симуляции выполнения работы.
     */
    fun getFreeDepartment(index: Int): Department {
        return departments[index]
    }
}