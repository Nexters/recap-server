package com.retoday.core.global.exception

abstract class ServerException(
    override val message: String,
    val code: String,
    val status: Int
) : RuntimeException(message) {
    constructor(errorType: ErrorType) :
        this(
            message = errorType.message,
            code = errorType.code,
            status = errorType.status
        )
}
