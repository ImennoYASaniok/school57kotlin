package ru.tbank.education.school.lesson6.creditriskanalyzer.rules_extra

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.PaymentRisk
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.LoanRepository
import ru.tbank.education.school.lesson6.creditriskanalyzer.repositories.OverdueRepository
import java.time.LocalDateTime

/**
 * Необходимо посчитать количество открытых кредитов с просрочкой больше 30 дней
 *
 * Как считать score:
 * - Если таких кредитов больше 3 → HIGH (слишком свежие)
 * - Если такой кредит один → MEDIUM
 * - Если таких кредитов нет → LOW
 */
class LoanCountRule(
    private val loanRepository: LoanRepository,
    private val overdueRepository: OverdueRepository
) : ScoringRule {


    override val ruleName: String = "Loan Count"

    override fun evaluate(client: Client): ScoringResult {
        val allOverdues = overdueRepository.getOverdues(client.id)
        // Как проверить в этой лямбде, что в allOverdues есть хотя бы один overdue, у которого overdue.loanId == loan.id (чтобы не писать !!)
        // Возможно просто написать доп условие allOverdues.find { overdue -> overdue.loanId == loan.id }.isNotEmpty, но получается два раза будет срабатывать allOverdues.find есть ли метод получше?
        val countLoan = loanRepository.getLoans(client.id).count { loan -> !loan.isClosed && allOverdues.find { overdue -> overdue.loanId == loan.id }!!.daysOverdue > 30 }

        val risk = when {
            countLoan > 3 -> PaymentRisk.HIGH
            countLoan > 0 -> PaymentRisk.MEDIUM
            else -> PaymentRisk.LOW
        }

        return ScoringResult(
            ruleName,
            risk
        )
    }
}