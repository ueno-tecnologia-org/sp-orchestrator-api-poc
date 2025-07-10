package com.uenobank.sporchestratorapi.business.ports

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.domain.entities.CommonSimulation
import java.sql.Date

/**
 * Inbound port for loan simulation operations
 * This port defines the contract for handling loan simulation requests
 */
interface SimulateLoanPort {
    /**
     * Handles a loan simulation request
     * @param request Common loan simulation parameters
     * @param expirationDate Loan expiration date
     * @param currency Currency code
     * @param modality Loan modality
     * @return Simulation result
     */
    suspend fun handleSimulation(
        request: CommonSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): Simulation
}
