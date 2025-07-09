package com.uenobank.sporchestratorapi.domain.entities

import java.math.BigDecimal

/**
 * Domain entity representing a loan simulation result
 */
data class LoanSimulation(
    val installments: AmountInstallment,
    val errorCode: String? = null,
    val errorMessage: String? = null
)

/**
 * Represents an individual loan installment
 */
@JvmInline
value class AmountInstallment (val value: BigDecimal)

/**
 * Common loan simulation parameters
 */
data class CommonLoanSimulation(
    val personCode: String,
    val requestId: String,
    val amount: BigDecimal,
    val termMonths: Int,
    val interestRate: BigDecimal? = null
)

/**
 * Request identifier for loan operations
 */
@JvmInline
value class RequestId(
    val value: String
)
