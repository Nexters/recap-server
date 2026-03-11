package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.Profile
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository : ListCrudRepository<Profile, Long>, CustomProfileRepository {
    fun findByUserId(userId: Long): Profile?
}
