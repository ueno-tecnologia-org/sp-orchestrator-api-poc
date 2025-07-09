package com.uenobank.sporchestratorapi.domain.entities

/**
 * Domain entity representing a currency
 */
data class Currency(
    val isoCode: String,
    val name: String?,
    val errorCode: String? = null,
    val errorMessage: String? = null
)
