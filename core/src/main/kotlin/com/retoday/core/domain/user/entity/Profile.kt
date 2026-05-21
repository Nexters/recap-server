package com.retoday.core.domain.user.entity

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalTime

@Table("profile")
data class Profile(
    val userId: Long,
    var firstName: String,
    var lastName: String,
    var imageUrl: String,
    val timeZone: TimeZone = TimeZone.SEOUL,
    val recapPeriod: LocalTime? = null,
    var language: Language = Language.KO
) : BaseEntity() {
    fun synchronizeOAuthUser(getOAuthUserResponse: GetOAuthUserResponse) {
        firstName = getOAuthUserResponse.firstName
        lastName = getOAuthUserResponse.lastName
        imageUrl = getOAuthUserResponse.imageUrl
    }
}
