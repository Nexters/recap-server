package com.retoday.core.domain.user.dto.projection

import com.retoday.core.domain.user.entity.Profile

data class ProfileWithEmailAndExcludedDomains(
    val profile: Profile,
    val email: String,
    val excludedDomains: List<String>
)
