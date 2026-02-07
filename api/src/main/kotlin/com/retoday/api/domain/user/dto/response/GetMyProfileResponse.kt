package com.retoday.api.domain.user.dto.response

import com.retoday.core.domain.user.dto.result.GetProfileByUserIdResult
import com.retoday.core.domain.user.entity.TimeZone
import java.time.LocalTime

data class GetMyProfileResponse(
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
        fun from(result: GetProfileByUserIdResult): GetMyProfileResponse =
            with(result) {
                GetMyProfileResponse(
                    id = id,
                    email = email,
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
