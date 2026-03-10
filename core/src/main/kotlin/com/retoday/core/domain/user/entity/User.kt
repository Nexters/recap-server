package com.retoday.core.domain.user.entity

import com.retoday.core.domain.auth.dto.response.GetOAuthUserResponse
import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("user")
data class User(
    val socialId: String,
    var email: String,
    val provider: Provider,
    val roles: String = Role.MEMBER.name,
    val isActive: Boolean = true
) : BaseEntity() {
    fun synchronizeOAuthUser(getOAuthUserResponse: GetOAuthUserResponse) {
        email = getOAuthUserResponse.email
    }
}
