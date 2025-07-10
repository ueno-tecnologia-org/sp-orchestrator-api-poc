package com.uenobank.sporchestratorapi.infrastructure.rest.mapper

import com.uenobank.sporchestratorapi.domain.entities.CommonSimulation
import com.uenobank.sporchestratorapi.infrastructure.rest.dto.SimulateLoanRequestDto
import org.springframework.stereotype.Component

@Component
class SimulationRequestMapper {

    fun toDomain(request: SimulateLoanRequestDto): CommonSimulation {
        return CommonSimulation(
            personCode = request.personCode,
            requestId = request.requestId,
            amount = request.amount,
            termMonths = request.termMonths,
            interestRate = request.interestRate
        )
    }
}
