package com.uenobank.sporchestratorapi.infrastructure.exceptions

class LoanSimulationLoopLimitExceededException(override val message: String): CoreBankingException(message) {
}