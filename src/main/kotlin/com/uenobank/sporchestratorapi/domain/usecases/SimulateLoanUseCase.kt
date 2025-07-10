package com.uenobank.sporchestratorapi.domain.usecases

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.domain.entities.CommonSimulation
import java.sql.Date

/**
 * Use case interface for loan simulation operations
 */
interface SimulateLoanUseCase {
    /**
     * Executes a loan simulation
     * @param request Common loan simulation parameters
     * @param expirationDate Loan expiration date
     * @param currency Currency code
     * @param modality Loan modality
     * @return Simulation result
     */
    suspend fun execute(
        request: CommonSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): Simulation
}
