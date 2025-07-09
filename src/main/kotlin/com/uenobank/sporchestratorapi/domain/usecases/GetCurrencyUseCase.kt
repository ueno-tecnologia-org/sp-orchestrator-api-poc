package com.uenobank.sporchestratorapi.domain.usecases

import com.uenobank.sporchestratorapi.domain.entities.Currency

/**
 * Use case interface for currency operations
 */
interface GetCurrencyUseCase {
    /**
     * Executes a currency retrieval by ISO code
     * @param isoCode Currency ISO code
     * @return Currency result
     */
    suspend fun execute(isoCode: String): Currency
}
