package com.retoday.api.global.exception

import com.retoday.api.global.dto.ErrorResponse
import com.retoday.core.global.exception.ServerException
import com.retoday.core.global.util.getLogger
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(basePackages = ["com.retoday.api"])
class GlobalExceptionHandler {
    private companion object {
        const val INTERNAL_SERVER_ERROR_CODE = "INTERNAL_SERVER_ERROR"
        const val INTERNAL_SERVER_ERROR_MESSAGE = "서버 오류가 발생했습니다."
        const val INVALID_JSON_MESSAGE = "JSON 형식이 올바르지 않습니다."
        const val EXCEPTION_SUFFIX = "_EXCEPTION"
        val logger = getLogger()
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
                            .removeSuffix(EXCEPTION_SUFFIX)
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
                        .fieldErrors
                        .joinToString(", ") { "${it.field}: ${it.defaultMessage}" }
            )
        )

    @ExceptionHandler(ConstraintViolationException::class)
    fun handle(exception: ConstraintViolationException): ResponseEntity<ErrorResponse> =
        handle(
            InvalidRequestException(
                message =
                    exception.constraintViolations
                        .joinToString(", ") { "${it.propertyPath.last()}: ${it.message}" }
            )
        )

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handle(exception: HttpMessageNotReadableException): ResponseEntity<ErrorResponse> =
        handle(InvalidRequestException(INVALID_JSON_MESSAGE))

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
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
