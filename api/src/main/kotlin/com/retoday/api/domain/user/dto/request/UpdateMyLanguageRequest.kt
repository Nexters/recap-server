package com.retoday.api.domain.user.dto.request

import com.retoday.core.domain.user.entity.Language

data class UpdateMyLanguageRequest(
    val language: Language
)
