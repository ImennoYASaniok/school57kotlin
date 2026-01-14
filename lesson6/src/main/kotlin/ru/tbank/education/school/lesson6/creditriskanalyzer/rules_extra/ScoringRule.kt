package ru.tbank.education.school.lesson6.creditriskanalyzer.rules_extra

import ru.tbank.education.school.lesson6.creditriskanalyzer.models.Client
import ru.tbank.education.school.lesson6.creditriskanalyzer.models.ScoringResult

interface ScoringRule {
    val ruleName: String
    fun evaluate(client: Client): ScoringResult
}