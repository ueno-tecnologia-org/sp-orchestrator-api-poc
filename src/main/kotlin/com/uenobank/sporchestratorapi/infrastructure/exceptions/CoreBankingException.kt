package com.uenobank.sporchestratorapi.infrastructure.exceptions

open class CoreBankingException(override val message: String): RuntimeException() {

    override fun toString(): String {
        return message
    }
}