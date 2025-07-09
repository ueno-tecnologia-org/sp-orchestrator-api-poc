package com.uenobank.sporchestratorapi.infrastructure.exceptions

/**
 * Exception thrown when database validation fails during stored procedure execution
 */
class DatabaseValidationException(
    message: String,
    cause: Throwable? = null
) : RuntimeException(message, cause)
