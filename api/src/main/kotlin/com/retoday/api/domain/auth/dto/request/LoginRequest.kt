package com.retoday.api.domain.auth.dto.request

import com.fasterxml.jackson.annotation.JsonProperty
import com.retoday.core.domain.auth.dto.command.LoginCommand
import com.retoday.core.domain.user.entity.Provider
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
