package com.retoday.core.domain.auth.dto.response

import com.retoday.core.domain.user.entity.Provider

data class GetOAuthUserResponse(
    val id: String,
    val provider: Provider,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String
)
