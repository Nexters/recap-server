package com.retoday.api.global.exception

import com.fasterxml.jackson.databind.ObjectMapper
import com.retoday.api.global.dto.ErrorResponse
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.security.web.access.AccessDeniedHandler

class SecurityExceptionHandler(
    private val objectMapper: ObjectMapper
) : AuthenticationEntryPoint,
    AccessDeniedHandler {
    private companion object {
        const val UNAUTHENTICATED_CODE = "UNAUTHENTICATED"
        const val UNAUTHENTICATED_MESSAGE = "인증되지 않은 사용자입니다."
        const val UNAUTHORIZED_CODE = "UNAUTHORIZED"
        const val UNAUTHORIZED_MESSAGE = "인가되지 않은 사용자입니다."
    }

    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException
    ) {
        with(response) {
            status = HttpStatus.UNAUTHORIZED.value()
            writeError(
                ErrorResponse(
                    code = UNAUTHENTICATED_CODE,
                    message = UNAUTHENTICATED_MESSAGE
                )
            )
        }
    }

    override fun handle(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AccessDeniedException
    ) {
        with(response) {
            status = HttpStatus.FORBIDDEN.value()
            writeError(
                ErrorResponse(
                    code = UNAUTHORIZED_CODE,
                    message = UNAUTHORIZED_MESSAGE
                )
            )
        }
    }

    private fun HttpServletResponse.writeError(response: ErrorResponse) {
        contentType = MediaType.APPLICATION_JSON_VALUE
        writer.write(objectMapper.writeValueAsString(response))
    }
}
