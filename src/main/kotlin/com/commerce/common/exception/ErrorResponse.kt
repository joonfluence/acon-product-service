package com.commerce.common.exception

class ErrorResponse<T>(
    val code: String,
    val message: String,
    val response: T,
) {
    companion object {
        fun of(
            errorCodes: ErrorCodes,
            response: Any?,
        ): ErrorResponse<Any?> {
            return ErrorResponse(errorCodes.code, errorCodes.message, response)
        }
    }
}
