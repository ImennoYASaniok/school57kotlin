package seminar.tasks

/**
 * Задание 3. Synchronized
 *
 * Исправьте задание 2 с помощью @Synchronized или synchronized {} блока,
 * чтобы результат всегда был 10000.
 */
object SynchronizedCounter {

    /**
     * @return финальное значение counter (должно быть ровно 10000)
     */
    fun run(): Int {
        var counter = 0
        val threads = mutableListOf<Thread>()
        val lock = Any()

        repeat(10) {
            val thread = Thread {
                repeat(1000) {
                    synchronized(lock) {
                        counter++
                    }
                }
            }
            threads.add(thread)
        }
        threads.forEach { it.start() }
        threads.forEach { it.join() }

        return counter
    }
}
