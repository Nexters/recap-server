package com.retoday.core.domain.user.dto.result

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailAndExcludedDomains
import com.retoday.core.domain.user.entity.TimeZone
import java.time.LocalTime

data class GetProfileByUserIdResult(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val timeZone: TimeZone,
    val recapPeriod: LocalTime?,
    val excludedDomains: List<String>
) {
    companion object {
        fun from(projection: ProfileWithEmailAndExcludedDomains): GetProfileByUserIdResult =
            with(projection.profile) {
                GetProfileByUserIdResult(
                    id = id!!,
                    email = projection.email,
                    firstName = firstName,
                    lastName = lastName,
                    imageUrl = imageUrl,
                    timeZone = timeZone,
                    recapPeriod = recapPeriod,
                    excludedDomains = projection.excludedDomains
                )
            }
    }
}
