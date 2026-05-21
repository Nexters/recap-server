package com.retoday.core.domain.user.dto.result

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailProjection
import com.retoday.core.domain.user.entity.Language
import com.retoday.core.domain.user.entity.TimeZone
import java.time.LocalTime

data class GetMyProfileResult(
    val id: Long,
    val email: String,
    val firstName: String,
    val lastName: String,
    val imageUrl: String,
    val timeZone: TimeZone,
    val recapPeriod: LocalTime?,
    val language: Language,
    val excludedDomains: List<String>
) {
    companion object {
        fun of(
            profileWithEmail: ProfileWithEmailProjection,
            excludedDomains: List<String>
        ): GetMyProfileResult =
            GetMyProfileResult(
                id = profileWithEmail.id,
                email = profileWithEmail.email,
                firstName = profileWithEmail.firstName,
                lastName = profileWithEmail.lastName,
                imageUrl = profileWithEmail.imageUrl,
                timeZone = profileWithEmail.timeZone,
                recapPeriod = profileWithEmail.recapPeriod,
                language = profileWithEmail.language,
                excludedDomains = excludedDomains
            )
    }
}
