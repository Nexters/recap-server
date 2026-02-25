package com.retoday.api.global.exception

import com.retoday.api.global.dto.ErrorResponse
import com.retoday.core.domain.history.exception.RateLimitExceededException
import com.retoday.core.global.alert.DiscordAlertService
import com.retoday.core.global.exception.ErrorType
import com.retoday.core.global.exception.ServerException
import com.retoday.core.global.extension.getLogger
import jakarta.servlet.http.HttpServletRequest
import jakarta.validation.ConstraintViolationException
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.HttpRequestMethodNotSupportedException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException

@RestControllerAdvice(basePackages = ["com.retoday.api"])
class GlobalExceptionHandler(
    private val alertService: DiscordAlertService
) {
    private companion object {
        const val INVALID_JSON_MESSAGE = "JSON 형식이 올바르지 않습니다."
        val logger = getLogger()
    }

    @ExceptionHandler(RateLimitExceededException::class)
    fun handle(exception: RateLimitExceededException): ResponseEntity<ErrorResponse> =
        with(exception) {
            logger.warn { message }

            val headers =
                HttpHeaders().apply {
                    set("Retry-After", retryAfterSeconds.toString())
                }

            val response =
                ErrorResponse(
                    code = code,
                    message = message
                )

            ResponseEntity
                .status(status)
                .headers(headers)
                .body(response)
        }

    @ExceptionHandler(ServerException::class)
    fun handle(exception: ServerException): ResponseEntity<ErrorResponse> =
        with(exception) {
            logger.warn { message }

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
        handle(InvalidRequestException(message = INVALID_JSON_MESSAGE))

    @ExceptionHandler(
        HttpRequestMethodNotSupportedException::class,
        MethodArgumentTypeMismatchException::class
    )
    fun handle(): ResponseEntity<ErrorResponse> = handle(InvalidRequestException())

    @ExceptionHandler(Exception::class)
    fun handle(
        exception: Exception,
        request: HttpServletRequest
    ): ResponseEntity<ErrorResponse> {
        logger.error(exception) { exception.message }
        alertService.send(
            """
            🔴 **[PROD] 서버 오류 발생**
            ${request.method} ${request.requestURI}
            error: ${exception.message}
            """.trimIndent()
        )

        return ResponseEntity
            .status(ErrorType.INTERNAL_SERVER_ERROR.status)
            .body(ErrorResponse.from(ErrorType.INTERNAL_SERVER_ERROR))
    }
}
