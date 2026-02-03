package com.retoday.api.global.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.retoday.api.global.dto.ErrorResponse
import com.retoday.core.global.exception.ErrorType
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler

class SecurityExceptionHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint,
    AccessDeniedHandler {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        with(response) {
            status = ErrorType.UNAUTHENTICATED.status.value()
            writeError(ErrorResponse.from(ErrorType.UNAUTHENTICATED))
        }
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException
    ) {
        with(response) {
            status = ErrorType.UNAUTHORIZED.status.value()
            writeError(ErrorResponse.from(ErrorType.UNAUTHORIZED))
        }
    }

    private fun HttpServletResponse.writeError(response: ErrorResponse) {
        contentType = MediaType.APPLICATION_JSON_VALUE
        writer.write(objectMapper.writeValueAsString(response))
    }
}
