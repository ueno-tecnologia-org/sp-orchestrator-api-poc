package com.uenobank.sporchestratorapi.infrastructure.rest

import com.uenobank.sporchestratorapi.business.ports.SimulateLoanPort
import com.uenobank.sporchestratorapi.business.ports.GetCurrencyPort
import com.uenobank.sporchestratorapi.domain.repositories.StoredProcedureRepository
import com.uenobank.sporchestratorapi.infrastructure.rest.dto.SimulateLoanRequestDto
import com.uenobank.sporchestratorapi.infrastructure.rest.dto.SimulateLoanResponseDto
import com.uenobank.sporchestratorapi.infrastructure.rest.mapper.LoanSimulationRequestMapper
import com.uenobank.sporchestratorapi.infrastructure.rest.mapper.LoanSimulationResponseMapper
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.responses.ApiResponses
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.sql.Date
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@RestController
@RequestMapping("/simulation")
@Tag(name = "Loan Simulation", description = "API para simulaciones de préstamos")
class LoanSimulationController(
    private val simulateLoanPort: SimulateLoanPort,
    private val getCurrencyPort: GetCurrencyPort,
    private val requestMapper: LoanSimulationRequestMapper,
    private val responseMapper: LoanSimulationResponseMapper
) {

    private val logger = LoggerFactory.getLogger(LoanSimulationController::class.java)

    @PostMapping("/")
    @Operation(
        summary = "Simular préstamo",
        description = "Ejecuta una simulación de préstamo con los parámetros proporcionados"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Simulación ejecutada exitosamente"),
            ApiResponse(responseCode = "400", description = "Request inválido"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    suspend fun simulateLoan(
        @Valid @RequestBody request: SimulateLoanRequestDto
    ): ResponseEntity<SimulateLoanResponseDto> {

        logger.info("Received loan simulation request for person: ${request.personCode}")

        try {
            // Convert string date to SQL Date
            val expirationDate = Date.valueOf(
                LocalDate.parse(request.expirationDate, DateTimeFormatter.ISO_LOCAL_DATE)
            )

            // Map request to domain
            val domainRequest = requestMapper.toDomain(request)

            // Execute simulation through the port
            val result = simulateLoanPort.handleLoanSimulation(
                request = domainRequest,
                expirationDate = expirationDate,
                currency = request.currency,
                modality = request.modality
            )

            // Map domain result to response
            val response = responseMapper.toDto(result)

            return if (result.errorCode == null) {
                logger.info("Loan simulation completed successfully for person: ${request.personCode}")
                ResponseEntity.ok(response)
            } else {
                logger.warn("Loan simulation failed for person: ${request.personCode}, error: ${result.errorCode}")
                ResponseEntity.badRequest().body(response)
            }

        } catch (exception: Exception) {
            logger.error("Error processing loan simulation request for person: ${request.personCode}", exception)

            val errorResponse = SimulateLoanResponseDto(
                installmentAmount = BigDecimal.ZERO,
                errorCode = "PROCESSING_ERROR",
                errorMessage = exception.message ?: "Unknown error during simulation processing",
                success = false
            )

            return ResponseEntity.status(500).body(errorResponse)
        }
    }

    @GetMapping("/currency/{isoCode}")
    @Operation(
        summary = "Obtener moneda por c��digo ISO",
        description = "Obtiene la información de una moneda utilizando su código ISO"
    )
    @ApiResponses(
        value = [
            ApiResponse(responseCode = "200", description = "Moneda encontrada exitosamente"),
            ApiResponse(responseCode = "404", description = "Moneda no encontrada"),
            ApiResponse(responseCode = "500", description = "Error interno del servidor")
        ]
    )
    suspend fun getCurrencyByISOCode(
        @PathVariable isoCode: String
    ): ResponseEntity<Map<String, Any?>> {

        logger.info("Received request to get currency for ISO code: {}", isoCode)

        return try {
            val result = getCurrencyPort.getCurrencyByISOCode(isoCode)

            if (result.errorCode == null) {
                logger.info("Currency found for ISO code {}: {}", isoCode, result.name)
                ResponseEntity.ok(mapOf(
                    "success" to true,
                    "isoCode" to result.isoCode,
                    "currency" to result.name
                ))
            } else {
                logger.warn("Currency not found for ISO code: {}", isoCode)
                ResponseEntity.status(404).body(mapOf(
                    "success" to false,
                    "isoCode" to result.isoCode,
                    "error" to result.errorCode,
                    "message" to result.errorMessage
                ))
            }

        } catch (exception: Exception) {
            logger.error("Error retrieving currency for ISO code: {}", isoCode, exception)
            ResponseEntity.status(500).body(mapOf(
                "success" to false,
                "isoCode" to isoCode,
                "error" to "PROCESSING_ERROR",
                "message" to (exception.message ?: "Unknown error occurred while retrieving currency")
            ))
        }
    }
}
