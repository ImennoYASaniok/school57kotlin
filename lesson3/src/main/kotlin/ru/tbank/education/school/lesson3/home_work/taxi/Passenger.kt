package ru.tbank.education.school.lesson3.home_work.taxi

const val DRIVER_ID_SEAT: Int = 0

interface InterfacePassengers {
    fun addOtherPassenger(otherPassenger: OtherPassenger?)
    fun getOutDriver()
    fun getOutPaidPassanger()
    fun getOutOtherPassanger(IdSeat: Int)
}
class Passengers(var driver: Driver? = null, var paidPassenger: PaidPassenger? = null, var otherPassengers: MutableList<OtherPassenger> = mutableListOf()): InterfacePassengers {
    override fun addOtherPassenger(otherPassenger: OtherPassenger?) { otherPassengers.add(otherPassenger!!) }
    override fun getOutDriver() { driver = null }
    override fun getOutPaidPassanger() { paidPassenger = null }
    override fun getOutOtherPassanger(IdSeat: Int) {
        val deleteIndexOtherPassengers: MutableList<Int> = mutableListOf()
        for (ind in 0..otherPassengers.size - 1) {
            if (otherPassengers[ind].IdSeat == IdSeat) { deleteIndexOtherPassengers.add(ind); }
        }
        if (deleteIndexOtherPassengers.size == 0) throw Exception("такого пассажира не существует")
        else {
            for (deleteIndex: Int in deleteIndexOtherPassengers) { otherPassengers.removeAt(deleteIndex) }
        }
    }
}

interface InterfacePassenger {

}
sealed class Passenger(val name: String, val age: Int = 0, var IdSeat: Int): InterfacePassenger {
    protected fun otherInit() {
        if (IdSeat == DRIVER_ID_SEAT) throw Exception("PassengerClassError: место IdSeat = ${DRIVER_ID_SEAT} за рулём занимает водитель")
    }

    open fun getInfo(): String {
        return "имя=${name}, возраст=${age}, место=${IdSeat}"
    }

    constructor(copy: Passenger) : this(copy.name, copy.age, copy.IdSeat)
}

class Driver(name: String, age: Int = 0, val licence: String, val driveAge: Int): Passenger(name, age, DRIVER_ID_SEAT) {
    constructor(copy: Driver) : this(copy.name, copy.age, copy.licence, copy.driveAge)
    override fun getInfo(): String {
        return "Водитель(${super.getInfo()}, лицензия='${licence}', вождение лет=${driveAge})"
    }
}

class PaidPassenger(name: String, age: Int = 0, IdSeat: Int, val creditCard: String): Passenger(name, age, IdSeat) {
    init { otherInit() }
    constructor(copy: PaidPassenger) : this(copy.name, copy.age, copy.IdSeat, copy.creditCard)
    override fun getInfo(): String {
        return "Пассажир, который платит(${super.getInfo()}, кредитка=${creditCard.take(4)}_****_****_****)"
    }
}

class OtherPassenger(name: String, age: Int = 0, IdSeat: Int): Passenger(name, age, IdSeat) {
    init { otherInit() }
    constructor(copy: PaidPassenger) : this(copy.name, copy.age, copy.IdSeat)
    override fun getInfo(): String {
        return "Обычный пассажир(${super.getInfo()})"
    }
}

