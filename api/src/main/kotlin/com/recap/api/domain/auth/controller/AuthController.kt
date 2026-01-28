package com.recap.api.domain.auth.controller

import com.recap.api.domain.auth.dto.request.LoginRequest
import com.recap.api.domain.auth.dto.response.LoginResponse
import com.recap.core.domain.auth.service.AuthService
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
        @RequestBody
        @Valid
        request: LoginRequest
    ): LoginResponse =
        authService
            .login(request.toCommand())
            .let { LoginResponse.from(it) }
}
