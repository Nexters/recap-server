package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.TimeZone
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface ProfileRepository :
    JpaRepository<Profile, Long>,
    CustomProfileRepository {
    @Query(
        """
        SELECT p
        FROM Profile p
        WHERE p.timeZone IN :timeZones
          AND p.userId IN (
              SELECT u.id
              FROM User u
              WHERE u.isActive = true
          )
        """
    )
    fun findAllActiveByTimeZones(
        @Param("timeZones")
        timeZones: Collection<TimeZone>
    ): List<Profile>

    fun findByUserId(userId: Long): Profile?
}
