package com.retoday.core.domain.user.dto.projection

import com.retoday.core.domain.user.entity.Profile

data class ProfileWithEmailProjection(
    val profile: Profile,
    val email: String
)
