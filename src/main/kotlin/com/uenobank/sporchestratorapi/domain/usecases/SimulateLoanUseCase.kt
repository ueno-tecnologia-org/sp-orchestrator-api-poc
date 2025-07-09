package com.uenobank.sporchestratorapi.domain.usecases

import com.uenobank.sporchestratorapi.domain.entities.LoanSimulation
import com.uenobank.sporchestratorapi.domain.entities.CommonLoanSimulation
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
     * @return LoanSimulation result
     */
    suspend fun execute(
        request: CommonLoanSimulation,
        expirationDate: Date,
        currency: String,
        modality: Int
    ): LoanSimulation
}
