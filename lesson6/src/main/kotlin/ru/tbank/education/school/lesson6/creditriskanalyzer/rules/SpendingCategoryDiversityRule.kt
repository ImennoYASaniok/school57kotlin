package ru.tbank.education.school.lesson6.creditriskanalyzer.rules

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository
import java.time.LocalDateTime

/**
 * Проверяет разнообразие категорий трат клиента.
 *
 * Идея:
 * - Получить все транзакции клиента за последние 3 месяца.
 * - Посчитать, в скольких уникальных категориях клиент тратит деньги.
 *
 * Как считать score:
 * - Если меньше 3 категорий → HIGH (тратит только на одно и то же)
 * - Если от 3 до 6 категорий → MEDIUM
 * - Если больше 6 категорий → LOW (разнообразное поведение, меньше риск)
 *
 */
class SpendingCategoryDiversityRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "Spending Category Diversity"

    override fun evaluate(client: Client): ScoringResult {
        val transactions = transactionRepo.getTransactions(client.id)
        val threeMonthsAgo = LocalDateTime.now().minusMonths(3)

        val uniqueCategories = arrayOfNulls<String>(TransactionCategory.entries.size)
        var uniqueCount = 0

        for (transaction in transactions) {
            if (!transaction.date.isBefore(threeMonthsAgo)) {
                var found = false
                for (k in 0..uniqueCount) {
                    if (uniqueCategories[k] == transaction.category.name) {
                        found = true
                        break
                    }
                }
                if (!found) {
                    uniqueCategories[uniqueCount] = transaction.category.name
                    uniqueCount += 1
                }
            }
        }

        val risk = when {
            uniqueCount < 3 -> PaymentRisk.HIGH
            uniqueCount <= 6 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }

        return ScoringResult(ruleName, risk)
    }
}