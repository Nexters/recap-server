package com.retoday.core.global.exception

import org.springframework.http.HttpStatus

abstract class ServerException(
    override val message: String,
    val code: String,
    val status: HttpStatus
) : RuntimeException(message) {
    constructor(errorType: ErrorType) :
        this(
            message = errorType.message,
            code = errorType.code,
            status = errorType.status
        )

    constructor(errorType: ErrorType, customMessage: String) : this(
        message = customMessage,
        code = errorType.code,
        status = errorType.status
    )
}
