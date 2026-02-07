package com.retoday.core.domain.user.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.entity.UserExcludedWebsite
import com.retoday.core.domain.website.entity.Website
import com.retoday.core.fixture.*
import io.kotest.core.test.TestCase
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

class CustomProfileRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var profileRepository: ProfileRepository
    private lateinit var user: User
    private lateinit var profile: Profile
    private lateinit var website: Website
    private lateinit var userExcludedWebsite: UserExcludedWebsite

    override suspend fun beforeEach(testCase: TestCase) {
        user =
            createUser(id = null)
                .save()
        profile =
            createProfile(id = null, userId = user.id!!)
                .save()
        website =
            createWebsite(id = null)
                .save()
        userExcludedWebsite =
            createUserExcludedWebsite(
                id = null,
                userId = user.id!!,
                websiteId = website.id!!
            ).save()
    }

    init {
        "findByUserIdWithEmailAndExcludedDomains()" {
            val projection = profileRepository.findByUserIdWithEmailAndExcludedDomains(user.id!!)

            projection
                .shouldNotBeNull()
                .shouldBeEqualToComparingFields(
                    createProfileWithEmailAndExcludedDomains(
                        profile = profile,
                        excludedDomains = listOf(website.domain)
                    )
                )
        }
    }
}
