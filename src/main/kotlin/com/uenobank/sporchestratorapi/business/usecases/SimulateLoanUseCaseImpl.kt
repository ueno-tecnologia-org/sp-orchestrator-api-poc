package com.uenobank.sporchestratorapi.business.usecases

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.domain.entities.CommonSimulation
import com.uenobank.sporchestratorapi.domain.repositories.StoredProcedureRepository
import com.uenobank.sporchestratorapi.domain.usecases.SimulateLoanUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import java.sql.Date

/**
 * Implementation of the loan simulation use case
 */
@Service
class SimulateLoanUseCaseImpl(
    private val storedProcedureRepository: StoredProcedureRepository
) : SimulateLoanUseCase {

    private val logger = LoggerFactory.getLogger(SimulateLoanUseCaseImpl::class.java)

    override suspend fun execute(
        request: CommonSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): Simulation = withContext(Dispatchers.IO) {

        logger.info(
            "Executing loan simulation for person: ${request.personCode}, " +
            "amount: ${request.amount}, term: ${request.termMonths} months"
        )

        try {
            val result = storedProcedureRepository.simulate(
                personCode = request.personCode,
                amount = request.amount,
                installments = request.termMonths,
                expirationDate = expirationDate,
                currency = currency,
                modality = modality,
                requestId = request.requestId.toIntOrNull()
            )

            logger.info("Loan simulation completed successfully for person: ${request.personCode}")
            result

        } catch (exception: Exception) {
            logger.error("Error executing loan simulation for person: ${request.personCode}", exception)
            Simulation(
                installments = com.uenobank.sporchestratorapi.domain.entities.AmountInstallment(java.math.BigDecimal.ZERO),
                errorCode = "SIMULATION_ERROR",
                errorMessage = exception.message ?: "Unknown error during simulation"
            )
        }
    }
}
