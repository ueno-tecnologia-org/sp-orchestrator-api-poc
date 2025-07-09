package com.uenobank.sporchestratorapi.business.ports

import com.uenobank.sporchestratorapi.domain.entities.LoanSimulation
import com.uenobank.sporchestratorapi.domain.entities.CommonLoanSimulation
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
     * @return LoanSimulation result
     */
    suspend fun handleLoanSimulation(
        request: CommonLoanSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): LoanSimulation
}
