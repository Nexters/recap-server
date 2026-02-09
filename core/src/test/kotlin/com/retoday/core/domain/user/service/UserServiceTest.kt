package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.website.repository.WebsiteRepository
import com.retoday.core.fixture.DOMAIN
import com.retoday.core.fixture.ID
import com.retoday.core.fixture.createGetMyProfileResult
import com.retoday.core.fixture.createProfileWithEmail
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk

class UserServiceTest : BehaviorSpec() {
    private val profileRepository = mockk<ProfileRepository>()
    private val websiteRepository = mockk<WebsiteRepository>()
    private val userService =
        UserService(
            profileRepository = profileRepository,
            websiteRepository = websiteRepository
        )

    init {
        Given("가입한 사용자가") {
            every { profileRepository.findByUserIdWithEmail(any()) } returns createProfileWithEmail()
            every { websiteRepository.findAllExcludedDomainsByUserId(any()) } returns listOf(DOMAIN)

            When("본인의 프로필을 조회하면") {
                val result = userService.getMyProfile(ID)

                Then("이메일과 예외 도메인을 포함한 프로필이 조회된다.") {
                    result shouldBeEqual createGetMyProfileResult()
                }
            }
        }
    }
}
