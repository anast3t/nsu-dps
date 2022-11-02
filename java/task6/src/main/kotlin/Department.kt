import java.util.concurrent.ThreadLocalRandom
import java.util.concurrent.TimeUnit

class Department(
    /**
     * @return Уникальный идентификатор для отдела,
     * по которому мы можем отличить его от других.
     */
    val identifier: Int
) {
    val workingSeconds: Int = ThreadLocalRandom.current().nextInt(1, 6)

    /**
     * ВАЖНО!
     * Далеко не самый правильный способ вычисления и получения данных,
     * но для демонстрации работы барьера пойдёт.
     *
     * @return Результат вычислений.
     */
    var calculationResult = 0
        private set

    /**
     * Симуляция работы длительностью в workingSeconds секунд.
     * В данном случае просто вычисляем сумму.
     */
    fun performCalculations() {
        for (i in 0 until workingSeconds) {
            try {
                Thread.sleep(TimeUnit.SECONDS.toMillis(1))
            } catch (ignored: InterruptedException) {
                // Ignored case.
            }
            calculationResult += i
        }
    }
}