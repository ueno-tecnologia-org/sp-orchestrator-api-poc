package com.uenobank.sporchestratorapi.infrastructure.adapters.inbound

import com.uenobank.sporchestratorapi.business.ports.SimulateLoanPort
import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.domain.entities.CommonSimulation
import com.uenobank.sporchestratorapi.domain.usecases.SimulateLoanUseCase
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.sql.Date

/**
 * Inbound adapter/handler for loan simulation operations
 * This handler implements the SimulateLoanPort and delegates to the use case
 */
@Component
class SimulateLoanHandler(
    private val simulateLoanUseCase: SimulateLoanUseCase
) : SimulateLoanPort {

    private val logger = LoggerFactory.getLogger(SimulateLoanHandler::class.java)

    override suspend fun handleSimulation(
        request: CommonSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): Simulation {

        logger.info("Handling loan simulation request for person: ${request.personCode}")

        return try {
            val result = simulateLoanUseCase.execute(
                request = request,
                expirationDate = expirationDate,
                currency = currency,
                modality = modality
            )

            logger.info("Loan simulation handled successfully for person: ${request.personCode}")
            result

        } catch (exception: Exception) {
            logger.error("Error handling loan simulation for person: ${request.personCode}", exception)
            throw exception
        }
    }
}
