package com.retoday.core.domain.user.dto.result

import com.retoday.core.domain.user.dto.projection.ProfileWithEmail
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
        fun of(
            profileWithEmail: ProfileWithEmail,
            excludedDomains: List<String>
        ): GetProfileByUserIdResult =
            with(profileWithEmail.profile) {
                GetProfileByUserIdResult(
                    id = id!!,
                    email = profileWithEmail.email,
                    firstName = firstName,
                    lastName = lastName,
                    imageUrl = imageUrl,
                    timeZone = timeZone,
                    recapPeriod = recapPeriod,
                    excludedDomains = excludedDomains
                )
            }
    }
}
