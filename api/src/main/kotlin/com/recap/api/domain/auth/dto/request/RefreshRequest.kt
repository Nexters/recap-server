package com.recap.api.domain.auth.dto.request

import com.recap.core.domain.auth.dto.command.RefreshCommand
import jakarta.validation.constraints.NotBlank

data class RefreshRequest(
    @field:NotBlank
    val refreshToken: String
) {
    fun toCommand(): RefreshCommand = RefreshCommand(refreshToken = refreshToken)
}
