package com.retoday.core.fixture

import com.retoday.core.domain.user.dto.projection.ProfileWithEmail
import com.retoday.core.domain.user.dto.result.GetProfileByUserIdResult
import com.retoday.core.domain.user.entity.*
import java.time.LocalTime

const val SOCIAL_ID = "1232342423"
const val EMAIL = "earlgrey02@retoday.com"
val PROVIDER = Provider.GOOGLE
val ROLES = setOf(Role.MEMBER)
const val IS_ACTIVE = true
const val FIRST_NAME = "Sangyoon"
const val LAST_NAME = "Jeong"
const val IMAGE_URL = "https://re-today.com/profile.png"
val TIME_ZONE = TimeZone.SEOUL
val RECAP_PERIOD: LocalTime = LocalTime.now()

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

fun createProfile(
    id: Long? = ID,
    userId: Long = ID,
    firstName: String = FIRST_NAME,
    lastName: String = LAST_NAME,
    imageUrl: String = IMAGE_URL,
    timeZone: TimeZone = TIME_ZONE,
    recapPeriod: LocalTime = RECAP_PERIOD
): Profile =
    Profile(
        id = id,
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        imageUrl = imageUrl,
        timeZone = timeZone,
        recapPeriod = recapPeriod
    )

fun createUserExcludedWebsite(
    id: Long? = ID,
    userId: Long = ID,
    websiteId: Long = ID
): UserExcludedWebsite =
    UserExcludedWebsite(
        id = id,
        userId = userId,
        websiteId = websiteId
    )

fun createProfileWithEmail(
    profile: Profile = createProfile(),
    email: String = EMAIL
): ProfileWithEmail =
    ProfileWithEmail(
        profile = profile,
        email = email
    )

fun createGetProfileByUserIdResult(
    id: Long = ID,
    email: String = EMAIL,
    firstName: String = FIRST_NAME,
    lastName: String = LAST_NAME,
    imageUrl: String = IMAGE_URL,
    timeZone: TimeZone = TIME_ZONE,
    recapPeriod: LocalTime? = RECAP_PERIOD,
    excludedDomains: List<String> = listOf(DOMAIN)
): GetProfileByUserIdResult =
    GetProfileByUserIdResult(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        imageUrl = imageUrl,
        timeZone = timeZone,
        recapPeriod = recapPeriod,
        excludedDomains = excludedDomains
    )
