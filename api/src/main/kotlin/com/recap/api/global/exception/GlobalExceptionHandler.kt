package com.recap.api.global.exception

import com.recap.api.global.dto.ErrorResponse
import com.recap.core.global.exception.ServerException
import com.recap.core.global.util.getLogger
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(basePackages = ["com.recap.api"])
class GlobalExceptionHandler {
    companion object {
        private const val INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR"
        private const val INTERNAL_SERVER_ERROR_MESSAGE = "서버 오류가 발생했습니다."
        private val logger = getLogger()
    }

    @ExceptionHandler(ServerException::class)
    fun handle(exception: ServerException): ResponseEntity<ErrorResponse> =
        with(exception) {
            logger.warn { message }

            val code =
                this::class
                    .simpleName!!
                    .run {
                        replace(Regex("([a-z])([A-Z])"), "$1_$2")
                            .uppercase()
                            .removeSuffix("_EXCEPTION")
                    }
            val response =
                ErrorResponse(
                    code = code,
                    message = message
                )

            ResponseEntity
                .status(status)
                .body(response)
        }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handle(exception: MethodArgumentNotValidException): ResponseEntity<ErrorResponse> =
        handle(
            InvalidRequestException(
                message =
                    exception.bindingResult
                        .allErrors
                        .mapNotNull { it.defaultMessage }
                        .joinToString(", ")
            )
        )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(exception: ConstraintViolationException): ResponseEntity<ErrorResponse> =
        handle(
            InvalidRequestException(
                message =
                    exception.constraintViolations
                        .joinToString(", ") { "${it.propertyPath.last()} ${it.message}" }
            )
        )

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        HttpMessageNotReadableException::class,
        MethodArgumentTypeMismatchException::class
    )
    fun handle(): ResponseEntity<ErrorResponse> = handle(InvalidRequestException())

    @ExceptionHandler(Exception::class)
    fun handle(exception: Exception): ResponseEntity<ErrorResponse> {
        logger.error(exception) { exception.message }

        val response =
            ErrorResponse(
                code = INTERNAL_SERVER_ERROR_CODE,
                message = INTERNAL_SERVER_ERROR_MESSAGE
            )

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .body(response)
    }
}
