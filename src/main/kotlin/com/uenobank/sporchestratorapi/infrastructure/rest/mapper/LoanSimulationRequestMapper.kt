package com.uenobank.sporchestratorapi.infrastructure.rest.mapper

import com.uenobank.sporchestratorapi.domain.entities.CommonLoanSimulation
import org.springframework.stereotype.Component

@Component
class LoanSimulationRequestMapper {

    fun toDomain(request: SimulateLoanRequestDto): CommonLoanSimulation {
        return CommonLoanSimulation(
            personCode = request.personCode,
            requestId = request.requestId,
            amount = request.amount,
            termMonths = request.termMonths,
            interestRate = request.interestRate
        )
    }
}
