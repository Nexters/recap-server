package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.dto.projection.ProfileWithEmail

interface CustomProfileRepository {
    fun findByUserIdWithEmail(userId: Long): ProfileWithEmail?
}
