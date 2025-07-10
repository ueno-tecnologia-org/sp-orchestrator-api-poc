package com.uenobank.sporchestratorapi.infrastructure.rest.mapper

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import com.uenobank.sporchestratorapi.infrastructure.rest.dto.SimulateLoanResponseDto
import org.springframework.stereotype.Component

@Component
class SimulationResponseMapper {

    fun toDto(domainResult: Simulation): SimulateLoanResponseDto {
        return SimulateLoanResponseDto(
            installmentAmount = domainResult.installments.value,
            errorCode = domainResult.errorCode,
            errorMessage = domainResult.errorMessage,
            success = domainResult.errorCode == null
        )
    }
}
