package com.uenobank.sporchestratorapi.business.usecases

import com.uenobank.sporchestratorapi.domain.entities.Currency
import com.uenobank.sporchestratorapi.domain.repositories.StoredProcedureRepository
import com.uenobank.sporchestratorapi.domain.usecases.GetCurrencyUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

/**
 * Implementation of the get currency use case
 */
@Service
class GetCurrencyUseCaseImpl(
    private val storedProcedureRepository: StoredProcedureRepository
) : GetCurrencyUseCase {

    private val logger = LoggerFactory.getLogger(GetCurrencyUseCaseImpl::class.java)

    override suspend fun execute(isoCode: String): Currency = withContext(Dispatchers.IO) {

        logger.info("Executing currency retrieval for ISO code: $isoCode")

        try {
            val currencyName = storedProcedureRepository.getCurrencyByISOCode(isoCode)

            if (currencyName != null) {
                logger.info("Currency retrieval completed successfully for ISO code: $isoCode")
                Currency(
                    isoCode = isoCode,
                    name = currencyName
                )
            } else {
                logger.warn("Currency not found for ISO code: $isoCode")
                Currency(
                    isoCode = isoCode,
                    name = null,
                    errorCode = "CURRENCY_NOT_FOUND",
                    errorMessage = "Currency not found for ISO code: $isoCode"
                )
            }

        } catch (exception: Exception) {
            logger.error("Error executing currency retrieval for ISO code: $isoCode", exception)
            Currency(
                isoCode = isoCode,
                name = null,
                errorCode = "CURRENCY_ERROR",
                errorMessage = exception.message ?: "Unknown error during currency retrieval"
            )
        }
    }
}
