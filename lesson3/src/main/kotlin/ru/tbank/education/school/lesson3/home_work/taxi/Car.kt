package ru.tbank.education.school.lesson3.home_work.taxi

data class Car(val brand: String = "", var engine: String = "", var seats: Int) {
    constructor(copy: Car) : this(copy.brand, copy.engine, copy.seats)
}