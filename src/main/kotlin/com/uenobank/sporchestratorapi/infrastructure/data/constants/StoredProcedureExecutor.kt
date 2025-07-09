package com.uenobank.sporchestratorapi.infrastructure.data.constants

import org.springframework.jdbc.core.JdbcTemplate
import java.math.BigDecimal
import java.sql.CallableStatement
import java.sql.Connection
import java.sql.Types
import org.slf4j.LoggerFactory

/**
 * Enhanced executor for stored procedures with Spring JdbcTemplate integration
 */
class StoredProcedureExecutor(private val jdbcTemplate: JdbcTemplate) {

    private val logger = LoggerFactory.getLogger(StoredProcedureExecutor::class.java)

    /**
     * Execute a stored procedure with return value using JdbcTemplate
     */
    fun executeWithReturn(
        schema: String,
        procedureName: String,
        parameters: List<Any>,
        returnType: Int = Types.VARCHAR
    ): String? {
        val callStatement = StoredProcedureBuilder.buildCallWithReturn(
            schema = schema,
            procedureName = procedureName,
            parameterCount = parameters.size
        )

        logger.debug("Executing stored procedure (with return): {}", callStatement)

        return jdbcTemplate.execute({ con: Connection ->
            val stmt = con.prepareCall(callStatement)
            stmt.registerOutParameter(1, returnType)

            // Set input parameters (starting from index 2)
            parameters.forEachIndexed { index, param ->
                setParameter(stmt, index + 2, param)
            }
            stmt
        }, { cs: CallableStatement ->
            cs.execute()
            cs.getString(1)
        })
    }

    /**
     * Execute a stored procedure without return value using JdbcTemplate
     */
    fun execute(
        schema: String,
        procedureName: String,
        parameters: List<Any>
    ): Boolean {
        val callStatement = StoredProcedureBuilder.buildCall(
            schema = schema,
            procedureName = procedureName,
            parameterCount = parameters.size
        )

        logger.debug("Executing stored procedure: {}", callStatement)

        return jdbcTemplate.execute({ con: Connection ->
            val stmt = con.prepareCall(callStatement)

            // Set input parameters
            parameters.forEachIndexed { index, param ->
                setParameter(stmt, index + 1, param)
            }
            stmt
        }, { cs: CallableStatement ->
            cs.execute()
        }) as Boolean
    }

    /**
     * Execute a stored procedure with multiple output parameters using JdbcTemplate
     */
    fun executeWithMultipleOutputs(
        schema: String,
        procedureName: String,
        inputParameters: List<Any?>,
        outputParameters: List<Pair<String, Int>>
    ): Map<String, Any?> {
        val callStatement = StoredProcedureBuilder.buildCall(
            schema = schema,
            procedureName = procedureName,
            parameterCount = inputParameters.size + outputParameters.size
        )

        logger.debug("Executing stored procedure with multiple outputs: {}", callStatement)

        return jdbcTemplate.execute({ con: Connection ->
            val stmt = con.prepareCall(callStatement)

            // Set input parameters first
            inputParameters.forEachIndexed { index, param ->
                setParameter(stmt, index + 1, param)
            }

            // Register output parameters
            outputParameters.forEachIndexed { index, (_, type) ->
                stmt.registerOutParameter(inputParameters.size + index + 1, type)
            }

            stmt
        }, { cs: CallableStatement ->
            cs.execute()

            // Extract output parameter values
            val results = mutableMapOf<String, Any?>()
            outputParameters.forEachIndexed { index, (name, type) ->
                val paramIndex = inputParameters.size + index + 1
                val value = when (type) {
                    Types.NUMERIC -> cs.getBigDecimal(paramIndex)
                    Types.VARCHAR -> cs.getString(paramIndex)
                    Types.INTEGER -> cs.getInt(paramIndex)
                    else -> cs.getObject(paramIndex)
                }
                results[name] = value
            }
            results
        }) as Map<String, Any?>
    }

    /**
     * Helper method to set parameters based on their type
     */
    private fun setParameter(stmt: CallableStatement, index: Int, param: Any?) {
        when {
            param is NullParameterType -> stmt.setNull(index, param.value)
            param is String -> stmt.setString(index, param)
            param is Int -> stmt.setInt(index, param)
            param is Long -> stmt.setLong(index, param)
            param is Double -> stmt.setDouble(index, param)
            param is Boolean -> stmt.setBoolean(index, param)
            param is java.sql.Date -> stmt.setDate(index, param)
            param is java.sql.Timestamp -> stmt.setTimestamp(index, param)
            param is BigDecimal -> stmt.setBigDecimal(index, param)
            else -> stmt.setString(index, param.toString())
        }
    }
}
