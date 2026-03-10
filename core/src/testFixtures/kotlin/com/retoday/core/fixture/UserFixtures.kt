package com.retoday.core.fixture

import com.retoday.core.domain.user.dto.projection.ProfileWithEmailProjection
import com.retoday.core.domain.user.dto.result.GetMyProfileResult
import com.retoday.core.domain.user.entity.*
import com.retoday.core.global.extension.createTsid
import java.time.Instant
import java.time.LocalTime
import java.time.temporal.ChronoUnit

const val SOCIAL_ID = "1232342423"
const val EMAIL = "earlgrey02@re-today.com"
val PROVIDER = Provider.GOOGLE
val ROLES = setOf(Role.MEMBER)
const val IS_ACTIVE = true
const val FIRST_NAME = "Sangyoon"
const val LAST_NAME = "Jeong"
const val IMAGE_URL = "https://re-today.com/profile.png"
val TIME_ZONE = TimeZone.SEOUL
val RECAP_PERIOD: LocalTime = LocalTime.now().truncatedTo(ChronoUnit.SECONDS)

fun createUser(
    id: Long? = ID,
    socialId: String = SOCIAL_ID,
    email: String = EMAIL,
    provider: Provider = PROVIDER,
    roles: Set<Role> = ROLES,
    isActive: Boolean = IS_ACTIVE,
    createdAt: Instant? = if (id == null) null else Instant.now()
): User =
    User(
        socialId = socialId,
        email = email,
        provider = provider,
        roles = roles.joinToString(","),
        isActive = isActive
    ).apply {
        this.id = id ?: createTsid()
        this.createdAt = createdAt
    }

fun createProfile(
    id: Long? = ID,
    userId: Long = ID,
    firstName: String = FIRST_NAME,
    lastName: String = LAST_NAME,
    imageUrl: String = IMAGE_URL,
    timeZone: TimeZone = TIME_ZONE,
    recapPeriod: LocalTime = RECAP_PERIOD,
    createdAt: Instant? = if (id == null) null else Instant.now()
): Profile =
    Profile(
        userId = userId,
        firstName = firstName,
        lastName = lastName,
        imageUrl = imageUrl,
        timeZone = timeZone,
        recapPeriod = recapPeriod
    ).apply {
        this.id = id ?: createTsid()
        this.createdAt = createdAt
    }

fun createUserExcludedWebsite(
    id: Long? = ID,
    userId: Long = ID,
    domain: String = USER_EX_DOMAIN,
    createdAt: Instant? = if (id == null) null else Instant.now()
): UserExcludedWebsiteDomain =
    UserExcludedWebsiteDomain(
        userId = userId,
        domain = domain
    ).apply {
        this.id = id ?: createTsid()
        this.createdAt = createdAt
    }

fun createProfileWithEmailProjection(
    profile: Profile = createProfile(),
    email: String = EMAIL
): ProfileWithEmailProjection =
    ProfileWithEmailProjection(
        id = profile.id,
        userId = profile.userId,
        firstName = profile.firstName,
        lastName = profile.lastName,
        imageUrl = profile.imageUrl,
        timeZone = profile.timeZone,
        recapPeriod = profile.recapPeriod,
        createdAt = profile.createdAt ?: Instant.now(),
        updatedAt = profile.updatedAt,
        deletedAt = profile.deletedAt,
        email = email
    )

fun createGetMyProfileResult(
    id: Long = ID,
    email: String = EMAIL,
    firstName: String = FIRST_NAME,
    lastName: String = LAST_NAME,
    imageUrl: String = IMAGE_URL,
    timeZone: TimeZone = TIME_ZONE,
    recapPeriod: LocalTime? = RECAP_PERIOD,
    excludedDomains: List<String> = listOf(USER_EX_DOMAIN)
): GetMyProfileResult =
    GetMyProfileResult(
        id = id,
        email = email,
        firstName = firstName,
        lastName = lastName,
        imageUrl = imageUrl,
        timeZone = timeZone,
        recapPeriod = recapPeriod,
        excludedDomains = excludedDomains
    )
