package com.uenobank.sporchestratorapi.infrastructure.adapters.inbound

import com.uenobank.sporchestratorapi.business.ports.GetCurrencyPort
import com.uenobank.sporchestratorapi.domain.entities.Currency
import com.uenobank.sporchestratorapi.domain.usecases.GetCurrencyUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

/**
 * Inbound adapter/handler for currency operations
 * This handler implements the GetCurrencyPort and delegates to the use case
 */
@Component
class GetCurrencyHandler(
    private val getCurrencyUseCase: GetCurrencyUseCase
) : GetCurrencyPort {

    private val logger = LoggerFactory.getLogger(GetCurrencyHandler::class.java)

    override suspend fun getCurrencyByISOCode(isoCode: String): Currency {

        logger.info("Handling currency retrieval request for ISO code: $isoCode")

        return try {
            val result = getCurrencyUseCase.execute(isoCode)

            logger.info("Currency retrieval handled successfully for ISO code: $isoCode")
            result

        } catch (exception: Exception) {
            logger.error("Error handling currency retrieval for ISO code: $isoCode", exception)
            throw exception
        }
    }
}
