package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.Profile
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository :
    JpaRepository<Profile, Long>,
    CustomProfileRepository
