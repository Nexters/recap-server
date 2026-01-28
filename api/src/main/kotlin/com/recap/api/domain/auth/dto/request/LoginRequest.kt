package com.recap.api.domain.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.recap.core.domain.auth.dto.command.LoginCommand
import com.recap.core.domain.user.entity.Provider
import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @field:NotBlank
    @get:JsonProperty("oAuthToken")
    val oAuthToken: String,
    val provider: Provider
) {
    fun toCommand(): LoginCommand =
        LoginCommand(
            oAuthToken = oAuthToken,
            provider = provider
        )
}
