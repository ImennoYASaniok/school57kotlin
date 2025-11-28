package ru.tbank.education.school.lesson8.homework.payments

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class PaymentProcessorTest {

    private val validCard = "4532015112830366"
    private val invalidLuhnCard = "4532015112830367"

    @Test
    fun `Неположительная сумма`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.processPayment(
                amount = 0,
                cardNumber = validCard,
                expiryMonth = 12,
                expiryYear = 2026,
                currency = "USD",
                customerId = "cust"
            )
        }
    }

    @Test
    fun `Недопустимый формат карты`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, "   ", 12, 2026, "USD", "c")
        }

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, "1234abcd5678", 12, 2026, "USD", "c")
        }

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, "123456789012", 12, 2026, "USD", "c")
        }
    }

    @Test
    fun `Карта просрочена`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 12, 2024, "USD", "c")
        }

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 10, 2025, "USD", "c")
        }
    }

    @Test
    fun `Невозможный месяц истечения срока`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 0, 2026, "USD", "c")
        }

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 13, 2026, "USD", "c")
        }
    }

    @Test
    fun `Пустая валюта или customerId`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 12, 2026, "", "c")
        }

        assertThrows<IllegalArgumentException> {
            processor.processPayment(100, validCard, 12, 2026, "USD", "")
        }
    }

    @Test
    fun `Подозрительный префиксы карточки`() {
        val processor = PaymentProcessor()
        val suspiciousCard = "4444123456789012"

        val result = processor.processPayment(100, suspiciousCard, 12, 2026, "USD", "cust")

        assertEquals("REJECTED", result.status)
    }

    @Test
    fun `Недествительные карты luhn`() {
        val processor = PaymentProcessor()

        val result = processor.processPayment(100, invalidLuhnCard, 12, 2026, "USD", "cust")

        assertEquals("REJECTED", result.status)
    }

    @Test
    fun `Нехватка средств`() {
        val processor = PaymentProcessor()
        val insufficientFundsCard = "5500123456789012"

        val result = processor.processPayment(100, insufficientFundsCard, 12, 2026, "USD", "cust")

        assertEquals("FAILED", result.status)
        assertEquals("Insufficient funds", result.message)
    }

    @Test
    fun `Превышен лимит транзакций`() {
        val processor = PaymentProcessor()

        val result = processor.processPayment(200_000, validCard, 12, 2026, "USD", "cust")

        assertEquals("FAILED", result.status)
        assertEquals("Transaction limit exceeded", result.message)
    }

    @Test
    fun `Проблема с таймаутом`() {
        val processor = PaymentProcessor()

        val result = processor.processPayment(34, validCard, 12, 2026, "USD", "cust")

        assertEquals("FAILED", result.status)
    }

    @Test
    fun `Успешный платёж`() {
        val processor = PaymentProcessor()

        val result = processor.processPayment(100, validCard, 12, 2026, "USD", "cust")

        assertEquals("SUCCESS", result.status)
        assertEquals("Payment completed", result.message)
    }

    @Test
    fun `Конвертация валют`() {
        val processor = PaymentProcessor()

        val eurResult = processor.processPayment(100, validCard, 12, 2026, "EUR", "c")
        val gbpResult = processor.processPayment(100, validCard, 12, 2026, "GBP", "c")
        val jpyResult = processor.processPayment(1, validCard, 12, 2026, "JPY", "c")
        val rubResult = processor.processPayment(1, validCard, 12, 2026, "RUB", "c")

        assertNotNull(eurResult)
        assertNotNull(gbpResult)
        assertNotNull(jpyResult)
        assertNotNull(rubResult)
    }

    @Test
    fun `Неподдерживаемая валюта`() {
        val processor = PaymentProcessor()
        val out = ByteArrayOutputStream()

        System.setOut(PrintStream(out))
        try {
            val result = processor.processPayment(100, validCard, 12, 2026, "ABC", "c")

            assertNotNull(result)
            assertTrue(out.toString().contains("Unsupported currency", ignoreCase = true))
        } finally {
            System.setOut(System.out)
        }
    }

    @Test
    fun `Рассчитывает скидку за лояльность для разных уровней`() {
        val processor = PaymentProcessor()

        assertEquals(0, processor.calculateLoyaltyDiscount(0, 10000))
        assertEquals(50, processor.calculateLoyaltyDiscount(500, 1000))
        assertEquals(1000, processor.calculateLoyaltyDiscount(2000, 10000))
        assertEquals(1500, processor.calculateLoyaltyDiscount(5000, 10000))
        assertEquals(2000, processor.calculateLoyaltyDiscount(10000, 10000))
    }

    @Test
    fun `Нулевая сумма для получения скидки за лояльность`() {
        val processor = PaymentProcessor()

        assertThrows<IllegalArgumentException> {
            processor.calculateLoyaltyDiscount(100, 0)
        }
    }

    @Test
    fun `процесс bulk пустой`() {
        val processor = PaymentProcessor()
        val out = ByteArrayOutputStream()

        System.setOut(PrintStream(out))
        try {
            val results = processor.bulkProcess(emptyList())

            assertTrue(results.isEmpty())
            assertTrue(out.toString().contains("No payments to process", ignoreCase = true))
        } finally {
            System.setOut(System.out)
        }
    }

    @Test
    fun `микс bulk процесс`() {
        val processor = PaymentProcessor()
        val payments = listOf(
            PaymentData(100, validCard, 12, 2026, "USD", "c1"),
            PaymentData(-10, validCard, 12, 2026, "USD", "c2"),
            null
        ) as List<PaymentData>

        val out = ByteArrayOutputStream()
        System.setOut(PrintStream(out))
        try {
            val results = processor.bulkProcess(payments)

            assertEquals(3, results.size)
            assertNotNull(results[0].status)
            assertEquals("REJECTED", results[1].status)
            assertEquals("FAILED", results[2].status)
        } finally {
            System.setOut(System.out)
        }
    }
}