package com.uenobank.sporchestratorapi.domain.repositories

import com.uenobank.sporchestratorapi.domain.entities.Simulation
import java.math.BigDecimal
import java.sql.Date

/**
 * Interface del repositorio para acceso a stored procedures
 */
interface StoredProcedureRepository {
    fun getCurrencyByISOCode(currencyIsoCode: String): String?
    fun simulate(
        personCode: String,
        amount: BigDecimal,
        installments: Int,
        expirationDate: Date,
        currency: String,
        modality: Int,
        requestId: Int? = null
    ): Simulation
}
