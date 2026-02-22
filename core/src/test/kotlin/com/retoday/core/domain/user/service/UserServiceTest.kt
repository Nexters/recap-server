package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.exception.ExcludedDomainAlreadyExistsException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserExcludedWebsiteRepository
import com.retoday.core.fixture.*
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.equals.shouldBeEqual
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class UserServiceTest : BehaviorSpec() {
    private val profileRepository = mockk<ProfileRepository>()
    private val userExcludedWebsiteRepository = mockk<UserExcludedWebsiteRepository>()
    private val userService =
        UserService(
            profileRepository = profileRepository,
            userExcludedWebsiteRepository = userExcludedWebsiteRepository
        )

    init {
        Given("가입한 사용자가") {
            every { profileRepository.findByUserIdWithEmail(any()) } returns createProfileWithEmail()
            every { userExcludedWebsiteRepository.findAllByUserId(any()) } returns
                listOf(createUserExcludedWebsite(domain = USER_EX_DOMAIN))

            When("본인의 프로필을 조회하면") {
                val result = userService.getMyProfile(ID)

                Then("이메일과 예외 도메인을 포함한 프로필이 조회된다.") {
                    result shouldBeEqual createGetMyProfileResult()
                }
            }
        }

        Given("사용자가 예외 도메인 추가를 요청하면") {
            val domain = "github.com"
            every { userExcludedWebsiteRepository.existsByUserIdAndDomain(any(), any()) } returns false
            every { userExcludedWebsiteRepository.save(any()) } returns createUserExcludedWebsite()

            When("아직 예외 도메인에 없는 도메인이라면") {
                userService.addMyExcludedDomain(ID, domain)

                Then("사용자 예외 도메인으로 저장된다.") {
                    verify(exactly = 1) { userExcludedWebsiteRepository.existsByUserIdAndDomain(ID, domain) }
                    verify(exactly = 1) {
                        userExcludedWebsiteRepository.save(
                            match {
                                it.userId == ID &&
                                    it.domain == domain
                            }
                        )
                    }
                }
            }
        }

        Given("사용자가 이미 추가된 예외 도메인을 다시 요청하면") {
            val domain = "github.com"
            every { userExcludedWebsiteRepository.existsByUserIdAndDomain(any(), any()) } returns true

            When("예외 도메인 추가를 요청하면") {
                Then("중복 저장 없이 처리된다.") {
                    shouldThrow<ExcludedDomainAlreadyExistsException> { userService.addMyExcludedDomain(ID, domain) }
                    verify(exactly = 0) { userExcludedWebsiteRepository.save(any()) }
                }
            }
        }
    }
}
