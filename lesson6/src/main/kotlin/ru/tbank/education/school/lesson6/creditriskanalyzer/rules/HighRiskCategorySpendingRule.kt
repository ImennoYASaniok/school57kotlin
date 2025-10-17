package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TicketTopic
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Transaction
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository

/**
 * Анализирует долю расходов клиента в рисковых категориях.
 *
 * Идея:
 * - Получить все транзакции клиента.
 * - Исключить доходы (SALARY).
 * - Посчитать общие сумму переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 * - Посчитать общие сумму всех переводов (без SALARY).
 * - Рассчитать долю переводов в категориях GAMBLING, CRYPTO, TRANSFER.
 *
 * Как считать risk:
 * - Если доля > 0.6 → HIGH
 * - Если доля > 0.3 → MEDIUM
 * - Иначе → LOW
 */
class HighRiskCategorySpendingRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "High-Risk Category Spending"

    override fun evaluate(client: Client): ScoringResult {
        var sumGambling: Long = 0
        var sumCrypto: Long = 0
        var sumTransfer: Long = 0
        var sumWithoutSalary: Long = 0
        for (transaction in transactionRepo.getTransactions(client.id)) {
            if (transaction.category != TransactionCategory.SALARY) {
                sumWithoutSalary += transaction.amount
                if (transaction.category == TransactionCategory.GAMBLING) sumGambling += transaction.amount
                else if (transaction.category == TransactionCategory.CRYPTO) sumCrypto += transaction.amount
                else if (transaction.category == TransactionCategory.TRANSFER) sumTransfer += transaction.amount
            }
        }
        var ratio = 0.0
        if (sumWithoutSalary.toInt() != 0) ratio = (sumGambling.toDouble() / sumWithoutSalary.toDouble()) + (sumCrypto.toDouble() / sumWithoutSalary.toDouble()) + (sumTransfer.toDouble() / sumWithoutSalary.toDouble())

        val risk = when {
            ratio > 0.6 -> PaymentRisk.HIGH
            ratio > 0.3 || sumWithoutSalary.toInt() == 0 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(
            ruleName,
            risk
        )
    }
}
