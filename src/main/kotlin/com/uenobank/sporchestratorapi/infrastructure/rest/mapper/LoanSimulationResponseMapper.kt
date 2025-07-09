package com.uenobank.sporchestratorapi.infrastructure.rest.mapper

import com.uenobank.sporchestratorapi.domain.entities.LoanSimulation
import org.springframework.stereotype.Component

@Component
class LoanSimulationResponseMapper {

    fun toDto(domainResult: LoanSimulation): SimulateLoanResponseDto {
        return SimulateLoanResponseDto(
            installmentAmount = domainResult.installments.value,
            errorCode = domainResult.errorCode,
            errorMessage = domainResult.errorMessage,
            success = domainResult.errorCode == null
        )
    }
}
