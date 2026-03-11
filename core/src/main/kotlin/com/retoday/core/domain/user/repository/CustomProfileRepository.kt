package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailProjection
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.TimeZone
import org.springframework.data.jdbc.repository.query.Query
import org.springframework.data.repository.query.Param

interface CustomProfileRepository {
    @Query(
        """
            SELECT
                p.*,
                u.email
            FROM profile p
            JOIN `user` u ON u.id = p.user_id
            WHERE u.id = :userId
        """
    )
    fun findByUserIdWithEmail(userId: Long): ProfileWithEmailProjection?

    @Query(
        """
            SELECT p.*
            FROM profile p
            JOIN `user` u ON u.id = p.user_id
            WHERE p.time_zone IN (:#{#timeZones.![name()]})
              AND u.is_active = true
        """
    )
    fun findAllActiveByTimeZones(
        @Param("timeZones")
        timeZones: Collection<TimeZone>
    ): List<Profile>
}
