package ru.tbank.education.school.lesson3.seminar.bank.accounts

import ru.tbank.education.school.lesson3.seminar.bank.models.Currency
import ru.tbank.education.school.lesson3.seminar.bank.models.Customer
import ru.tbank.education.school.lesson3.seminar.bank.models.DepositTransaction
import ru.tbank.education.school.lesson3.seminar.bank.models.WithdrawalTransaction
import kotlin.compareTo

class CashBackAccount(
    id: String,
    owner: Customer,
    currency: Currency,
    val cashBack: Double = 0.0
) : Account(id, owner, currency) {
    init {
        if (cashBack < 0 || cashBack > 1) throw Exception("Не корректные значения кешбека")
    }

    override fun deposit(amount: Int, description: String) : Boolean {
        balance += amount
        record(DepositTransaction("T-${transactions.size + 1}", this, amount, description))
        return true
    }

    override fun withdraw(amount: Int, description: String): Boolean {
        var extra_ammount = (amount.toDouble() * cashBack).toInt()
        if (balance + extra_ammount < amount) return false
        balance -= amount
        balance += extra_ammount
        println("DEBUG -> ${balance}")
        record(WithdrawalTransaction("T-${transactions.size + 1}", this, amount, description))
        return true
    }
}