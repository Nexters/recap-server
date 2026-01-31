package com.retoday.core.fixture

import com.retoday.core.domain.user.entity.Provider
import com.retoday.core.domain.user.entity.Role
import com.retoday.core.domain.user.entity.User

const val SOCIAL_ID = "1232342423"
const val EMAIL = "earlgrey02@github.com"
val PROVIDER = Provider.GOOGLE
val ROLES = setOf(Role.MEMBER)
const val IS_ACTIVE = true

fun createUser(
    id: Long? = ID,
    socialId: String = SOCIAL_ID,
    email: String = EMAIL,
    provider: Provider = PROVIDER,
    roles: Set<Role> = ROLES,
    isActive: Boolean = IS_ACTIVE
): User =
    User(
        id = id,
        socialId = socialId,
        email = email,
        provider = provider,
        roles = roles,
        isActive = isActive
    )
