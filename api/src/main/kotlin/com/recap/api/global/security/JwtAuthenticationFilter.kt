package com.recap.api.global.security

import com.recap.core.global.jwt.JwtProvider
import io.jsonwebtoken.JwtException
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpHeaders
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JwtAuthenticationFilter(
    private val jwtProvider: JwtProvider
) : OncePerRequestFilter() {
    private companion object {
        const val AUTHORIZATION_HEADER_PREFIX = "Bearer "
        const val INVALID_AUTHORIZATION_HEADER_MESSAGE = "Authorization header is invalid."
    }

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        request
            .getHeader(HttpHeaders.AUTHORIZATION)
            ?.run {
                runCatching { jwtProvider.extractPayload(getBearerToken()) }
                    .onSuccess { SecurityContextHolder.getContext().authentication = RecapAuthentication.from(it) }
                    .onFailure { if (it !is JwtException) throw it }
            }

        filterChain.doFilter(request, response)
    }

    private fun String.getBearerToken(): String =
        if (startsWith(AUTHORIZATION_HEADER_PREFIX)) {
            removePrefix(AUTHORIZATION_HEADER_PREFIX)
        } else {
            throw JwtException(INVALID_AUTHORIZATION_HEADER_MESSAGE)
        }
}
