package com.retoday.api.domain.history.dto.request

import jakarta.validation.constraints.Size

data class PageMetadata(
    @field:Size(max = 5000, message = "description은 5000자를 초과할 수 없습니다")
    val description: String?,
    @field:Size(max = 500, message = "faviconUrl은 500자를 초과할 수 없습니다")
    val faviconUrl: String?
)
