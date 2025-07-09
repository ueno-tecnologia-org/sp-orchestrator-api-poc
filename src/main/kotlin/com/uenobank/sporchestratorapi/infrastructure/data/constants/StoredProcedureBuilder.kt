package com.uenobank.sporchestratorapi.infrastructure.data.constants

/**
 * Utility class for building stored procedure call statements dynamically
 */
object StoredProcedureBuilder {

    /**
     * Builds a stored procedure call statement with return value
     * @param schema Database schema (e.g., "IT")
     * @param procedureName Name of the stored procedure (e.g., "FUE_OBT_ISO_MONEDA")
     * @param parameterCount Number of input parameters
     * @return Formatted call statement like "{? = call IT.FUE_OBT_ISO_MONEDA(?)}"
     */
    fun buildCallWithReturn(schema: String, procedureName: String, parameterCount: Int): String {
        val parameters = generateParameterPlaceholders(parameterCount)
        return "{? = call $schema.$procedureName($parameters)}"
    }

    /**
     * Builds a stored procedure call statement without return value
     * @param schema Database schema (e.g., "IT")
     * @param procedureName Name of the stored procedure
     * @param parameterCount Number of input parameters
     * @return Formatted call statement like "{call IT.FUE_OBT_ISO_MONEDA(?)}"
     */
    fun buildCall(schema: String, procedureName: String, parameterCount: Int): String {
        val parameters = generateParameterPlaceholders(parameterCount)
        return "{call $schema.$procedureName($parameters)}"
    }

    /**
     * Generates parameter placeholders (?, ?, ?)
     */
    private fun generateParameterPlaceholders(count: Int): String {
        return if (count == 0) "" else "?".repeat(count).chunked(1).joinToString(", ")
    }

    /**
     * Convenience method for common scenarios
     */
    fun buildITCall(procedureName: String, parameterCount: Int, hasReturnValue: Boolean = true): String {
        return if (hasReturnValue) {
            buildCallWithReturn(StoredProcedureConstants.Schemas.IT, procedureName, parameterCount)
        } else {
            buildCall(StoredProcedureConstants.Schemas.IT, procedureName, parameterCount)
        }
    }
}
