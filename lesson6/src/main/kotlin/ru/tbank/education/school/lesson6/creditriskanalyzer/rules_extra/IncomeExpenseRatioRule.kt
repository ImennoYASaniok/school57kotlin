package ru.tbank.education.school.lesson6.creditriskanalyzer.rules_extra

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.TransactionRepository
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Transaction
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.TransactionCategory
import java.time.LocalDateTime

/**
 * Анализирует соотношение доходов и расходов клиента за последние 3 месяца.
 *
 * Идея:
 * - Получить все транзакции клиента за последние 3 месяца.
 * - Разделить их на доходы (категория SALARY) и расходы (все остальные).
 * - Посчитать общую сумму доходов и расходов.
 * - Определить финансовое равновесие клиента.
 *
 * Как считать score:
 * - Если расходы > доходов → HIGH (клиент тратит больше, чем зарабатывает)
 * - Если расходы примерно равны доходам (±20%) → MEDIUM
 * - Если доходы значительно больше расходов → LOW
 *
 */
class IncomeExpenseRatioRule(
    private val transactionRepo: TransactionRepository
) : ScoringRule {

    override val ruleName: String = "Loan Count"

    override fun evaluate(client: Client): ScoringResult {
        val threeMonthsAgo = LocalDateTime.now().minusMonths(3)

        val filterTransaction = transactionRepo.getTransactions(client.id).filter { it.date.isAfter(threeMonthsAgo) }

        val sumSalary: Long = filterTransaction.filter { it.category == TransactionCategory.SALARY }.sumOf { it.amount }
        val sumOther: Long = filterTransaction.filter { it.category != TransactionCategory.SALARY }.sumOf { it.amount }

        val risk = when {
            filterTransaction.isEmpty() || sumOther > 1.2 * sumSalary -> PaymentRisk.HIGH
            sumOther >= 0.8 * sumSalary -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }
        return ScoringResult(
            ruleName,
            risk
        )
    }
}
