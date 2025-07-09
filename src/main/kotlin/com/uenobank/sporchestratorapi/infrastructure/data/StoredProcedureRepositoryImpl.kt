package com.uenobank.sporchestratorapi.infrastructure.data

import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants.ErrorMessages.CURRENCY_NOT_FOUND_MESSAGE
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants.ProcedureNames.FUE_OBT_ISO_MONEDA
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants.SimulationDBConstants.P_CLAVE_DEFAULT
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants.SimulationDBConstants.P_GRABAR_DEFAULT
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureExecutor
import com.uenobank.sporchestratorapi.domain.entities.LoanSimulation
import com.uenobank.sporchestratorapi.domain.entities.AmountInstallment
import com.uenobank.sporchestratorapi.domain.repositories.StoredProcedureRepository
import com.uenobank.sporchestratorapi.infrastructure.data.constants.NullParameterType
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants
import com.uenobank.sporchestratorapi.infrastructure.data.constants.StoredProcedureConstants.SimulationDBConstants.P_MTO_CONSOLIDADO_DEFAULT
import com.uenobank.sporchestratorapi.infrastructure.exceptions.CoreBankingException
import com.uenobank.sporchestratorapi.infrastructure.exceptions.DatabaseValidationException
import com.uenobank.sporchestratorapi.infrastructure.exceptions.LoanSimulationLoopLimitExceededException
import io.github.resilience4j.bulkhead.annotation.Bulkhead
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import io.github.resilience4j.ratelimiter.annotation.RateLimiter
import org.slf4j.LoggerFactory
import org.springframework.cache.annotation.Cacheable
import org.springframework.dao.DataAccessException
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.stereotype.Repository
import java.math.BigDecimal
import java.sql.*

@Repository
class StoredProcedureRepositoryImpl(
    jdbcTemplate: JdbcTemplate,
    private val fallbackService: StoredProcedureFallbackService
): StoredProcedureRepository {

    private val logger = LoggerFactory.getLogger(StoredProcedureRepositoryImpl::class.java)
    private val storedProcedureExecutor = StoredProcedureExecutor(jdbcTemplate)

    /**
     * Execute getCurrencyByISOCode using the builder pattern
     */
    @Cacheable(value = ["currency"], key = "#currencyIsoCode")
    @CircuitBreaker(name = "currency-service", fallbackMethod = "getCurrencyFallback")
    @RateLimiter(name = "currency-service", fallbackMethod = "getCurrencyFallback")
    @Bulkhead(name = "currency-service", fallbackMethod = "getCurrencyFallback")
    override fun getCurrencyByISOCode(currencyIsoCode: String): String? {
        logger.info("Fetching currency for currencyIsoCode: {}", currencyIsoCode)

        val currency = storedProcedureExecutor.executeWithReturn(
            schema = StoredProcedureConstants.Schemas.IT,
            procedureName = FUE_OBT_ISO_MONEDA,
            parameters = listOf(currencyIsoCode)
        )

        val currencyReturn = currency?.takeIf {
            !it.equals(CURRENCY_NOT_FOUND_MESSAGE, ignoreCase = true)
        }

        if (currencyReturn != null) {
            logger.info("Fetched currency: {} for currencyIsoCode: {}", currency, currencyIsoCode)
        } else {
            logger.error("Currency: {} not found", currencyIsoCode)
        }

        return currencyReturn
    }

    /**
     * Simulate loan using storedProcedureExecutor.executeWithMultipleOutputs
     */
    @CircuitBreaker(name = "loan-simulation-service", fallbackMethod = "simulateFallback")
    @RateLimiter(name = "loan-simulation-service", fallbackMethod = "simulateFallback")
    @Bulkhead(name = "loan-simulation-service", fallbackMethod = "simulateFallback")
    override fun simulate(
        personCode: String,
        amount: BigDecimal,
        installments: Int,
        expirationDate: Date,
        currency: String,
        modality: Int,
        requestId: Int?
    ): LoanSimulation {
        return try {
            // Define input parameters with explicit Any type casting
            val inputParameters = listOf<Any?>(
                personCode,                    // p_cod_persona
                amount,                        // p_mto_solicitado
                installments,                  // p_can_cuotas
                expirationDate,               // p_fec_pri_vencimiento
                NullParameterType(Types.NUMERIC),                         // p_cod_canal (NULL)
                currency,                     // p_cod_moneda
                modality,                     // p_cod_modalidad
                P_GRABAR_DEFAULT,             // p_grabar
                P_MTO_CONSOLIDADO_DEFAULT,                            // p_mto_consolidado
                P_CLAVE_DEFAULT,              // p_clave
                requestId ?: NullParameterType(Types.NUMERIC)                // p_solicitud
            )

            // Define output parameters
            val outputParameters = listOf(
                "p_cuotas" to Types.NUMERIC,
                "p_cod_error" to Types.NUMERIC,
                "p_msj_error" to Types.VARCHAR
            )

            // Execute stored procedure with multiple outputs
            val result = storedProcedureExecutor.executeWithMultipleOutputs(
                schema = StoredProcedureConstants.Schemas.IT,
                procedureName = "prp_gen_sol_prestamos",
                inputParameters = inputParameters,
                outputParameters = outputParameters
            )

            val codError = (result["p_cod_error"] as? Number)?.toInt() ?: 0
            val message = result["p_msj_error"] as? String
            val amountInstallmentRes = result["p_cuotas"] as? BigDecimal


            if (codError == 22) {
                val errorMessage = "Simulation failed for personCode: $personCode, amount: $amount, installments: $installments, dueDate: $expirationDate. With error code: $codError and message: $message"
                logger.error(errorMessage)
                throw LoanSimulationLoopLimitExceededException(errorMessage)
            }
            if (codError != 0) {
                val errorMessage = "Simulation failed for personCode: $personCode, amount: $amount, installments: $installments, dueDate: $expirationDate. With error code: $codError and message: $message"
                logger.error(errorMessage)
                throw CoreBankingException(errorMessage)
            } else {
                val amountInstallment = amountInstallmentRes ?: BigDecimal.ZERO
                val request = requestId?.toString() ?: "0"
                logger.info("Simulation successful for personCode: {}, request: {}, amountInstallment: {}", personCode, request, amountInstallment)

                LoanSimulation(
                    installments = AmountInstallment(amountInstallment),
                    errorCode = null,
                    errorMessage = null
                )
            }
        } catch (e: DataAccessException) {
            val cause = e.rootCause
            val personCodeMessage = "personCode: $personCode."
            if (cause is SQLException) {
                val errorMessage = cause.message
                logger.error("SQL Error executing DB SP prp_gen_sol_prestamos for: {} {}", personCodeMessage, errorMessage, cause)
                throw DatabaseValidationException("An SQL Error occurred while executing the DB SP prp_gen_sol_prestamos for: $personCodeMessage $errorMessage")
            }
            logger.error("An error occurred while executing the DB SP prp_gen_sol_prestamos for: {} {}", personCodeMessage, e.message)
            throw e
        }
    }

    /**
     * Fallback method for simulate
     */
    private fun simulateFallback(
        personCode: String,
        amount: BigDecimal,
        installments: Int,
        expirationDate: Date,
        currency: String,
        modality: Int,
        requestId: Int?,
        ex: Exception
    ): LoanSimulation {
        return fallbackService.simulateFallback(personCode, ex)
    }

    /**
     * Fallback method for getCurrencyByISOCode
     */
    private fun getCurrencyFallback(currencyIsoCode: String, ex: Exception): String? {
        return fallbackService.getCurrencyFallback(currencyIsoCode, ex)
    }
}
