package me.dgahn.vms.exception

import kotlinx.datetime.LocalDateTime
import kotlinx.serialization.Serializable
import me.dgahn.vms.ex.toKLocalDateTime

@Serializable
data class ErrorResponse(
    val code: String,
    val message: String,
    val trace: String,
    val timestamp: LocalDateTime = java.time.LocalDateTime.now().toKLocalDateTime()
) {
    companion object {
        fun of(errorCode: ErrorCode, trace: String = "") = ErrorResponse(
            code = errorCode.code,
            message = errorCode.message,
            trace = trace
        )
    }
}
