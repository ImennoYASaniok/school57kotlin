package ru.tbank.education.school.lesson3.home_work.taxi

interface InterfaceTaxi {
}

class Taxi(val brand: String = "", var engine: String = "", val seats: Int, var passengers: Passengers = Passengers()): InterfaceTaxi {
    private val car: Car = Car(brand, engine, seats)
    private val EMPLTY_ORDER = Order()
    var order: Order = EMPLTY_ORDER
        set(inputOrder: Order) {
            if (inputOrder.price < 0) throw Exception(EXCEPTION_PRICE)
            field = inputOrder
        }

    private val EMPTY_PASSENGERS = Passengers()
    private var StatusPoint = TypePoints.GARRAGE
    private val StatusPointInfo = mapOf(
        TypePoints.START_POINT to "Малина на точке сбора пассажиров",
        TypePoints.MIDDLE_POINT to "Малина на промежуточной точке высадки",
        TypePoints.END_POINT to "Малина на точке конца поездки",
        TypePoints.GARRAGE to "Машина в гараже"
    )
    val a = mapOf(1 to "one", 2 to "two", 3 to "three")

    private val EXCEPTION_SEATS = "TaxiError: Отрицательное число пассажирских мест"
    private val EXCEPTION_PRICE = "TaxiError: Отрицательная цена заказа"
    init {
        if (seats <= 0) throw Exception(EXCEPTION_SEATS)
        if (order.price < 0) throw Exception(EXCEPTION_PRICE)

    }

    constructor(copy: Taxi): this(
        copy.brand, copy.engine, copy.seats,
        copy.passengers
    )
    constructor(car: Car, passengers: Passengers): this(
        car.brand, car.engine, car.seats,
        passengers
    )

    fun setPassangers(driver: Driver? = null, paidPassenger: PaidPassenger? = null, otherPassengers: MutableList<OtherPassenger> = mutableListOf()) {
        passengers = Passengers(driver, paidPassenger, otherPassengers)
    }
    fun clearPassangers() { passengers = EMPTY_PASSENGERS }
    fun setDriver(driver: Driver?) { passengers.driver = driver }
    fun setPaidPassenger(paidPassenger: PaidPassenger?) { passengers.paidPassenger = paidPassenger }
    fun SetOtherPassengers(otherPassengers: MutableList<OtherPassenger>) { passengers.otherPassengers = otherPassengers }
    fun addOtherPassenger(otherPassenger: OtherPassenger?) { passengers.addOtherPassenger(otherPassenger) }
    fun getOutPassenger(IdSeat: Int) {
        if (passengers.driver!!.IdSeat == IdSeat) passengers.driver = null
        else if (passengers.paidPassenger!!.IdSeat == IdSeat) {
            if (order != EMPLTY_ORDER) throw Exception("это пассажир, который платит, перед тем как высадить его он должен заплатить")
            else passengers.paidPassenger = null
        }
        else passengers.getOutOtherPassanger(IdSeat)
    }
    fun getOutAllOtherPassengers() {
        if (passengers.paidPassenger != null) getOutPassenger(passengers.paidPassenger!!.IdSeat)
        passengers.otherPassengers = mutableListOf()
    }

    fun getStatus(): String {
        var passagersInfo: String = ""
        var orderInfo: String = ""
        if (passengers.driver == null) passagersInfo += "Водителя нет"
        else passagersInfo += passengers.driver!!.getInfo()
        passagersInfo += "\n"
        if (passengers.paidPassenger == null) passagersInfo += "Пассажира, который платит нет"
        else passagersInfo += passengers.paidPassenger!!.getInfo()
        passagersInfo += "\n"
        if (order == EMPLTY_ORDER) orderInfo += "Заказа нет"
        else orderInfo = order.getInfo()
        if (passengers.otherPassengers.size == 0) passagersInfo += "Остальных пассажиров нет" + "\n"
        else { for (passenger: OtherPassenger in passengers.otherPassengers) { passagersInfo += passenger.getInfo() + "\n" } }
        return "Текущий статус:\nМесто: ${StatusPointInfo[StatusPoint]}\nЗаказ: ${orderInfo}\nПассажиры:\n${passagersInfo}\n"
    }

    fun payOrder() { order = EMPLTY_ORDER }

    fun goTo(place: TypePoints) { StatusPoint = place }
}