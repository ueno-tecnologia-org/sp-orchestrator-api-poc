package com.uenobank.sporchestratorapi.business.ports

import com.uenobank.sporchestratorapi.domain.entities.Currency

/**
 * Inbound port for currency operations
 * This port defines the contract for handling currency requests
 */
interface GetCurrencyPort {
    /**
     * Handles a currency retrieval request by ISO code
     * @param isoCode Currency ISO code
     * @return Currency result
     */
    suspend fun getCurrencyByISOCode(isoCode: String): Currency
}
