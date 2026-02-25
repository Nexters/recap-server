package com.retoday.core.domain.user.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import com.retoday.core.fixture.createProfile
import com.retoday.core.fixture.createProfileWithEmailProjection
import com.retoday.core.fixture.createUser
import io.kotest.core.test.TestCase
import io.kotest.matchers.equality.shouldBeEqualToComparingFields
import io.kotest.matchers.nulls.shouldNotBeNull
import org.springframework.beans.factory.annotation.Autowired

class CustomProfileRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var profileRepository: ProfileRepository
    private lateinit var user: User
    private lateinit var profile: Profile

    override suspend fun beforeEach(testCase: TestCase) {
        user =
            createUser(id = null)
                .save()
        profile =
            createProfile(id = null, userId = user.id!!)
                .save()
    }

    init {
        "findByUserIdWithEmail()" {
            val profileWithEmail = profileRepository.findByUserIdWithEmail(user.id!!)

            profileWithEmail
                .shouldNotBeNull()
                .shouldBeEqualToComparingFields(createProfileWithEmailProjection(profile = profile))
        }
    }
}
