package ru.tbank.education.school.lesson3.home_work

import ru.tbank.education.school.lesson3.home_work.taxi.Driver
import ru.tbank.education.school.lesson3.home_work.taxi.Order
import ru.tbank.education.school.lesson3.home_work.taxi.OtherPassenger
import ru.tbank.education.school.lesson3.home_work.taxi.PaidPassenger
import ru.tbank.education.school.lesson3.home_work.taxi.Taxi
import ru.tbank.education.school.lesson3.home_work.taxi.TypePoints

fun main() {
    println("Создание такси")
    val taxi = Taxi(brand = "Kamaz", engine = "V12", seats = 4)
    print(taxi.getStatus())

    // val orderBadRequest = Order(price = -1000, startPoint = "Bad request", endPoint = "") //Exception Отрицательная цена заказа

    println("Получение заказа")
    val firstOrder = Order(price = 1000, startPoint = "Moscow", endPoint = "Planet Mars")
    taxi.order = firstOrder
    print(taxi.getStatus())

    println("Указания водителя")
    val firstDriver = Driver(name = "Bogdan", age = 52, licence = "Some licence text", driveAge = 4)
    taxi.setDriver(firstDriver)
    print(taxi.getStatus())

    println("Выезд на заказ")
    taxi.goTo(place = TypePoints.START_POINT)
    print(taxi.getStatus())

    println("Посадка пассажиров")
    val firstPaidPassenger = PaidPassenger(name = "Sasha", age = 15, IdSeat = 1, creditCard ="1234123412341234")
    // taxi.addOtherPassenger(OtherPassenger(name="Sasha", age=15, IdSeat=-1)) // Exception: searId=-1, невозможное значение
    // taxi.addOtherPassenger(OtherPassenger(name="Sasha", age=15, IdSeat=4)) // Exception: нет такого id, всего 4 места
    taxi.setPaidPassenger(firstPaidPassenger)
    print(taxi.getStatus())

    taxi.addOtherPassenger(OtherPassenger(name="NPC1", age=15, IdSeat=2))
    // taxi.addOtherPassenger(OtherPassenger(name="NPC1", age=15, IdSeat=0)) // Exception: searId=0, это место водителя
    taxi.addOtherPassenger(OtherPassenger(name = "NPC2", age = 15, IdSeat = 3))
    // taxi.addOtherPassenger(OtherPassenger(name="NPC2", age=15, IdSeat=2)) // Exception: searId=2, это место уже занято
    print(taxi.getStatus())

    println("Поездка до точки высадки промежуточно и высадка пассажира место=2")
    taxi.goTo(place=TypePoints.MIDDLE_POINT)
    taxi.getOutPassenger(IdSeat=2)
    // taxi.getOutPassenger(IdSeat=100) // Exception: такого пассажира не существует
    // taxi.getOutPassenger(IdSeat=1) // Exception: это пассажир, который платит, перед тем как высадить его он должен заплатить
    print(taxi.getStatus())

    print("Поездка до конечной точки")
    taxi.goTo(place = TypePoints.END_POINT)
    print(taxi.getStatus())

    println("Выплата за поездку")
    taxi.payOrder()
    taxi.getOutPassenger(IdSeat=1)
    taxi.getOutAllOtherPassengers()
    print(taxi.getStatus())

    println("Возврат в офис")
    taxi.goTo(place=TypePoints.GARRAGE)
    taxi.getOutPassenger(IdSeat=0) // Выход водителя
    print(taxi.getStatus())
}
