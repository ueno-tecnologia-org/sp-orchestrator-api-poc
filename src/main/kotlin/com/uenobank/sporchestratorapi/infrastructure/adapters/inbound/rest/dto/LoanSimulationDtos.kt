package com.uenobank.sporchestratorapi.infrastructure.adapters.inbound.rest.dto

import io.swagger.v3.oas.annotations.media.Schema
import jakarta.validation.constraints.*
import java.math.BigDecimal

/**
 * Request DTO for loan simulation
 */
@Schema(description = "Request para simulación de préstamo")
data class SimulateLoanRequestDto(
    @field:NotBlank(message = "Person code is required")
    @Schema(description = "Código de la persona", example = "P12345")
    val personCode: String,

    @field:NotBlank(message = "Request ID is required")
    @Schema(description = "ID de la solicitud", example = "REQ-2025-001")
    val requestId: String,

    @field:NotNull(message = "Amount is required")
    @field:DecimalMin(value = "0.01", message = "Amount must be greater than 0")
    @Schema(description = "Monto del préstamo", example = "50000.00")
    val amount: BigDecimal,

    @field:NotNull(message = "Term in months is required")
    @field:Min(value = 1, message = "Term must be at least 1 month")
    @field:Max(value = 360, message = "Term cannot exceed 360 months")
    @Schema(description = "Plazo en meses", example = "24")
    val termMonths: Int,

    @field:NotBlank(message = "Expiration date is required")
    @Schema(description = "Fecha de vencimiento (yyyy-MM-dd)", example = "2027-07-08")
    val expirationDate: String,

    @field:NotBlank(message = "Currency is required")
    @field:Size(min = 3, max = 3, message = "Currency must be 3 characters")
    @Schema(description = "Código de moneda", example = "USD")
    val currency: String,

    @field:NotNull(message = "Modality is required")
    @field:Min(value = 1, message = "Modality must be a positive number")
    @Schema(description = "Modalidad del préstamo", example = "1")
    val modality: Int,

    @Schema(description = "Tasa de interés (opcional)", example = "15.5")
    val interestRate: BigDecimal? = null
)

/**
 * Response DTO for loan simulation
 */
@Schema(description = "Response de simulación de préstamo")
data class SimulateLoanResponseDto(
    @Schema(description = "Monto de la cuota", example = "2145.67")
    val installmentAmount: BigDecimal,

    @Schema(description = "Código de error (si existe)")
    val errorCode: String? = null,

    @Schema(description = "Mensaje de error (si existe)")
    val errorMessage: String? = null,

    @Schema(description = "Indica si la simulación fue exitosa", example = "true")
    val success: Boolean = true
)

/**
 * Simulation parameters DTO for response
 */
@Schema(description = "Parámetros de la simulación")
data class SimulationParamsDto(
    @Schema(description = "Código de la persona", example = "P12345")
    val personCode: String,

    @Schema(description = "ID de la solicitud", example = "REQ-2025-001")
    val requestId: String,

    @Schema(description = "Monto del préstamo", example = "50000.00")
    val amount: BigDecimal,

    @Schema(description = "Plazo en meses", example = "24")
    val termMonths: Int,

    @Schema(description = "Moneda", example = "USD")
    val currency: String,

    @Schema(description = "Modalidad", example = "1")
    val modality: Int
)
