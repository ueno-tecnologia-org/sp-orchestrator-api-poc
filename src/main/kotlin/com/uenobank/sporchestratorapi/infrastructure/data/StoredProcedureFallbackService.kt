package com.uenobank.sporchestratorapi.infrastructure.data

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.domain.entities.AmountInstallment
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class StoredProcedureFallbackService {

    private val logger = LoggerFactory.getLogger(StoredProcedureFallbackService::class.java)

    /**
     * Fallback method for getCurrencyByISOCode
     */
    fun getCurrencyFallback(currencyIsoCode: String, ex: Exception): String? {
        logger.warn("Currency service fallback triggered for currencyIsoCode: {} due to: {}", currencyIsoCode, ex.message)
        return null
    }

    /**
     * Fallback method for simulate - simplified version with only essential parameters
     */
    fun simulateFallback(
        personCode: String,
        ex: Exception
    ): Simulation {
        logger.warn("Loan simulation service fallback triggered for personCode: {} due to: {}", personCode, ex.message)
        return Simulation(
            installments = AmountInstallment(BigDecimal.ZERO),
            errorCode = null,
            errorMessage = "Service temporarily unavailable. Please try again later."
        )
    }

    /**
     * Enhanced fallback for loan simulation with custom error message
     */
    fun simulateFallbackWithMessage(
        personCode: String,
        customMessage: String,
        ex: Exception
    ): Simulation {
        logger.warn("Loan simulation service fallback triggered for personCode: {} due to: {}", personCode, ex.message)
        return Simulation(
            installments = AmountInstallment(BigDecimal.ZERO),
            errorCode = null,
            errorMessage = customMessage
        )
    }
}
