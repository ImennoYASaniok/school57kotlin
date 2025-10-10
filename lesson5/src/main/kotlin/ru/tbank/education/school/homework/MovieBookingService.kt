package ru.tbank.education.school.homework

/**
 * Исключение, которое выбрасывается при попытке забронировать занятое место
 */
class SeatAlreadyBookedException(message: String) : Exception(message)

/**
 * Исключение, которое выбрасывается при попытке забронировать место при отсутствии свободных мест
 */
class NoAvailableSeatException(message: String) : Exception(message)

data class BookedSeat(
    val movieId: String, // идентификатор фильма
    val seat: Int // номер места
)

class MovieBookingService(
    private val maxQuantityOfSeats: Int // Максимальное кол-во мест
) {
    private var countSeats = 0
    private val dataSeats = mutableMapOf<String, MutableList<Int>>()

    init {
        if (maxQuantityOfSeats <= 0) throw IllegalArgumentException("Ошибка: максимальное число мест неположительное")
    }

    fun printSeats() {
        for (iMovieId in dataSeats.keys) {
            print(iMovieId); print(": "); for (iSeat in dataSeats[iMovieId]!!) { print(iSeat); print(" ") }; println()
        }
        println()
    }

    /**
     * Бронирует указанное место для фильма.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws IllegalArgumentException если номер места вне допустимого диапазона
     * @throws NoAvailableSeatException если нет больше свободных мест
     * @throws SeatAlreadyBookedException если место уже забронировано
     */
    fun bookSeat(movieId: String, seat: Int) {
        if (seat < 0 || seat >= maxQuantityOfSeats) throw IllegalArgumentException("Ошибка: номер места превышает максимальное количество мест")
        if (countSeats >= maxQuantityOfSeats) throw NoAvailableSeatException("Ошибка: нет свободных мест")
        if (isSeatBooked(movieId, seat)) throw SeatAlreadyBookedException("Ошибка: место уже забронировано")

        if (movieId in dataSeats.keys) dataSeats[movieId]!!.add(seat)
        else dataSeats[movieId] = mutableListOf(seat)
        countSeats += 1
    }

    /**
     * Отменяет бронь указанного места.
     *
     * @param movieId идентификатор фильма
     * @param seat номер места
     * @throws NoSuchElementException если место не было забронировано
     */
    fun cancelBooking(movieId: String, seat: Int) {
        if (!isSeatBooked(movieId, seat)) throw NoSuchElementException("Ошибка: место не было забронировано")

        val currMoveIdSeats = dataSeats[movieId]!!
        currMoveIdSeats.remove(seat)
        dataSeats[movieId] = currMoveIdSeats
        countSeats -= 1
    }

    /**
     * Проверяет, забронировано ли место
     *
     * @return true если место занято, false иначе
     */
    fun isSeatBooked(movieId: String, seat: Int): Boolean {
        if (movieId in dataSeats.keys) {
            val currMoveIdSeats = dataSeats[movieId]!!
            var isBooked = false
            for (iSeat in currMoveIdSeats) {
                if (iSeat == seat) isBooked = true
            }
            return isBooked
        } else return false
    }
}

fun main() {
    // val movieBookingServiceException = MovieBookingService(0) // IllegalArgumentException
    val movieBookingService = MovieBookingService(3)

    movieBookingService.bookSeat("321", 0)
    // movieBookingService.bookSeat("321", 0) // SeatAlreadyBookedException
    movieBookingService.bookSeat("321", 1)
    // movieBookingService.bookSeat("666", 3) // IllegalArgumentException
    movieBookingService.bookSeat("777", 0)
    //movieBookingService.bookSeat("555", 0) // NoAvailableSeatException
    movieBookingService.printSeats()

    // movieBookingService.cancelBooking("777", 1) // NoSuchElementException
    movieBookingService.cancelBooking("321", 0)
    movieBookingService.printSeats()

    println(movieBookingService.isSeatBooked("123", 0)) // false
    println(movieBookingService.isSeatBooked("777", 1)) // false
    println(movieBookingService.isSeatBooked("777", 0)) // true
}
