package com.retoday.api.domain.auth.controller

import com.retoday.api.domain.auth.dto.request.LoginRequest
import com.retoday.api.domain.auth.dto.request.RefreshRequest
import com.retoday.api.domain.auth.dto.response.LoginResponse
import com.retoday.api.domain.auth.dto.response.RefreshResponse
import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.auth.service.AuthService
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    fun login(
        @Valid
        @RequestBody
        request: LoginRequest
    ): LoginResponse =
        authService
            .login(request.toCommand())
            .let { LoginResponse.from(it) }

    @PostMapping("/refresh")
    fun refresh(
        @Valid
        @RequestBody
        request: RefreshRequest
    ): RefreshResponse =
        authService
            .refresh(request.toCommand())
            .let { RefreshResponse.from(it) }

    @PostMapping("/logout")
    fun logout(
        @AuthenticationId
        userId: Long
    ) {
        authService.logout(userId)
    }
}
