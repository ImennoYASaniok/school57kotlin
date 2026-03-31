package ru.tbank.education.school.practice.exceptions

/**
 * Интерфейс для управления заказами в магазине.
 */
interface OrderService {

    /**
     * Добавляет товар в корзину.
     *
     * @param productId ID товара
     * @param quantity количество
     * @throws IllegalArgumentException если quantity <= 0
     */
    fun addToCart(productId: String, quantity: Int)

    /**
     * Удаляет товар из корзины.
     *
     * @param productId ID товара
     * @throws NoSuchElementException если товара нет в корзине
     */
    fun removeFromCart(productId: String)

}

data class OrderItemImpl (
    val productId: String,
    val quantity: Int
)

class OrderServiceImpl: OrderService {
    val cart: MutableList<OrderItemImpl> = mutableListOf()

    override fun addToCart(productId: String, quantity: Int) {
        if (quantity <= 0) throw IllegalArgumentException("Ошибка: Попытка добавить продукт в не положительном количестве")
        cart.add(OrderItemImpl(productId, quantity))
    }

    override fun removeFromCart(productId: String) {
        if (!cart.removeIf { it.productId == productId }) throw NoSuchElementException("Ошибка: продукт не найден")
    }

}

fun main() {
    val orderService: OrderServiceImpl = OrderServiceImpl()

    for (ind in 1..10) orderService.addToCart(ind.toString(), 11 - ind)
    // orderService.addToCart("11", 0)
    println("Корзина:"); for (ordertem: OrderItemImpl in orderService.cart) println("productId = ${ordertem.productId}, quantality = ${ordertem.quantity}")
    orderService.removeFromCart("10")
    println("Корзина:"); for (ordertem: OrderItemImpl in orderService.cart) println("productId = ${ordertem.productId}, quantality = ${ordertem.quantity}")
    orderService.removeFromCart("12")
}