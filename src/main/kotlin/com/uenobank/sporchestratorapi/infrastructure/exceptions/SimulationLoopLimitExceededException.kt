package com.uenobank.sporchestratorapi.infrastructure.exceptions

class SimulationLoopLimitExceededException(override val message: String): CoreBankingException(message) {
}