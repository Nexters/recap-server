package com.retoday.api.domain.user.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class DeleteMyExcludedDomainRequest(
    @field:NotBlank
    @field:Size(max = 255)
    @field:Pattern(
        regexp = "^(?!-)(?:[a-zA-Z0-9-]{1,63}\\.)+[a-zA-Z]{2,63}$",
        message = "유효한 도메인 형식이 아닙니다."
    )
    val domain: String
)
