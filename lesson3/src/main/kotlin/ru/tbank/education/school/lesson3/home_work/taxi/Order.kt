package ru.tbank.education.school.lesson3.home_work.taxi

interface InterfaceOrder {
    fun getInfo(): String
}

class Order(val price: Int = 0, val startPoint: String = "", val endPoint: String = ""): InterfaceOrder {
    constructor(copy: Order) : this(copy.price, copy.startPoint, copy.endPoint)

    override fun getInfo(): String {
        return "цена=${price}, стартовая точка='${startPoint}', конечная точка='${endPoint}'"
    }
}