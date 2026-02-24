package com.retoday.api.domain.user.dto.request

import com.retoday.api.global.validation.Domain
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class AddMyExcludedDomainRequest(
    @field:NotBlank
    @field:Size(max = 255)
    @field:Domain
    val domain: String
)
