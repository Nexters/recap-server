package com.retoday.core.domain.user.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.repository.UserRepository
import com.retoday.core.fixture.createProfile
import com.retoday.core.fixture.createUser
import io.kotest.core.test.TestCase
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

class CustomProfileRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var profileRepository: ProfileRepository

    @Autowired
    private lateinit var userRepository: UserRepository

    private lateinit var user: User
    private lateinit var profile: Profile

    override suspend fun beforeEach(testCase: TestCase) {
        super.beforeEach(testCase)
        user =
            userRepository.save(createUser(id = null))
        profile =
            profileRepository.save(createProfile(id = null, userId = user.id))
    }

    init {
        "findByUserIdWithEmail()" {
            val profileWithEmail = profileRepository.findByUserIdWithEmail(user.id)

            profileWithEmail.shouldNotBeNull()
            profileWithEmail.id shouldBe profile.id
            profileWithEmail.userId shouldBe profile.userId
            profileWithEmail.firstName shouldBe profile.firstName
            profileWithEmail.lastName shouldBe profile.lastName
            profileWithEmail.imageUrl shouldBe profile.imageUrl
            profileWithEmail.timeZone shouldBe profile.timeZone
            profileWithEmail.recapPeriod shouldBe profile.recapPeriod
            profileWithEmail.language shouldBe profile.language
            profileWithEmail.email shouldBe user.email
            profileWithEmail.deletedAt shouldBe null
        }
    }
}
