package com.retoday.api.global.dto

import com.retoday.core.global.exception.ErrorType

data class ErrorResponse(
    val code: String,
    val message: String
) {
    companion object {
        fun from(errorType: ErrorType): ErrorResponse =
            with(errorType) {
                ErrorResponse(
                    code = code,
                    message = message
                )
            }
    }
}
