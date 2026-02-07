package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailAndExcludedDomains

interface CustomProfileRepository {
    fun findByUserIdWithEmailAndExcludedDomains(userId: Long): ProfileWithEmailAndExcludedDomains?
}
