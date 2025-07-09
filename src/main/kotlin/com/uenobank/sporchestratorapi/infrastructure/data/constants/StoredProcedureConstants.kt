package com.uenobank.sporchestratorapi.infrastructure.data.constants

import org.slf4j.Logger
import org.slf4j.LoggerFactory

/**
 * Constants related to stored procedure operations
 */
object StoredProcedureConstants {

    // Database Schemas
    object Schemas {
        const val IT = "IT"
    }

    // Stored Procedure Names
    object ProcedureNames {
        const val FUE_OBT_ISO_MONEDA = "FUE_OBT_ISO_MONEDA"
        const val SQL_PRP_GEN_SOL_PRESTAMOS = "prp_gen_sol_prestamos"
    }

    // Common Parameters
    object Parameters {
        const val USER_ID = "userId"
        const val ACCOUNT_ID = "accountId"
        const val TRANSACTION_ID = "transactionId"
        const val AMOUNT = "amount"
        const val CURRENCY = "currency"
    }

    // Error Messages
    object ErrorMessages {
        const val PROCEDURE_NOT_FOUND = "Stored procedure not found"
        const val EXECUTION_FAILED = "Failed to execute stored procedure"
        const val INVALID_PARAMETERS = "Invalid parameters provided"
        const val CONNECTION_FAILED = "Database connection failed"
        const val CURRENCY_NOT_FOUND_MESSAGE = "Moneda no registrada en el Sistema"
    }

    // Timeouts (in milliseconds)
    object Timeouts {
        const val DEFAULT_TIMEOUT = 30000L
        const val LONG_RUNNING_TIMEOUT = 60000L
        const val SHORT_TIMEOUT = 5000L
    }

    object SimulationDBConstants {
        const val P_GRABAR_DEFAULT = "S"
        const val P_MTO_CONSOLIDADO_DEFAULT = 0
        const val P_CLAVE_DEFAULT = 1234
        const val PENDING_VALIDATION_STATE = "S"
        const val PENDING_LIQUIDATION_STATE = "L"
    }
}
