package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailProjection

interface CustomProfileRepository {
    fun findByUserIdWithEmail(userId: Long): ProfileWithEmailProjection?
}
