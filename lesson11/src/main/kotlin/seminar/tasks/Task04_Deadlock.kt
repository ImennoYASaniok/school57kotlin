package seminar.tasks

/**
 * Задание 4. Deadlock
 *
 * Создайте пример deadlock с двумя ресурсами и двумя потоками.
 * Затем исправьте его.
 */
object Deadlock {

    /**
     * Демонстрация deadlock.
     * Внимание: этот метод зависнет! Используйте только для демонстрации.
     */
    fun runDeadlock() {
        val resourceA = Any()
        val recourceB = Any()

        val thread1 = Thread {
            synchronized(resourceA) {
                println("thread1: lock resourceA")
                Thread.sleep(500)
                synchronized(recourceB) {
                    Thread.sleep(500)
                    println("thread1: lock resourceB")
                }
            }
        }

        val thread2 = Thread {
            synchronized(resourceA) {
                println("thread1: lock resourceA")
                Thread.sleep(500)
                synchronized(recourceB) {
                    Thread.sleep(500)
                    println("thread1: lock resourceB")
                }
            }
        }

        thread1.start()
        thread2.start()
    }

    /**
     * Исправленная версия без deadlock.
     * Захватывайте ресурсы в одинаковом порядке.
     *
     * @return true если оба потока успешно завершились
     */
    fun runFixed(): Boolean {
        val resourceA = Any()
        val recourceB = Any()

        val thread1 = Thread {
            synchronized(resourceA) {
                println("thread1: lock resourceA")
                Thread.sleep(500)
                synchronized(recourceB) {
                    Thread.sleep(500)
                    println("thread1: lock resourceB")
                }
            }
        }

        val thread2 = Thread {
            synchronized(resourceA) {
                println("thread1: lock resourceA")
                Thread.sleep(500)
                synchronized(recourceB) {
                    Thread.sleep(500)
                    println("thread1: lock resourceB")
                }
            }
        }

        thread1.start()
        thread2.start()
        thread1.join()
        thread2.join()

        return true
    }
}
