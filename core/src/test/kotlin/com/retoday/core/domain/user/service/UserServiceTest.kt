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
import org.springframework.dao.DataIntegrityViolationException

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
            every { profileRepository.findByUserIdWithEmail(any()) } returns createProfileWithEmailProjection()
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
            val domain = "GitHub.COM"
            val normalizedDomain = "github.com"
            every { userExcludedWebsiteRepository.save(any()) } returns
                createUserExcludedWebsite(domain = normalizedDomain)

            When("아직 예외 도메인에 없는 도메인이라면") {
                userService.addMyExcludedDomain(ID, domain)

                Then("사용자 예외 도메인으로 저장된다.") {
                    verify(exactly = 1) {
                        userExcludedWebsiteRepository.save(
                            match {
                                it.userId == ID &&
                                    it.domain == normalizedDomain
                            }
                        )
                    }
                }
            }
        }

        Given("사용자가 이미 추가된 예외 도메인을 다시 요청하면") {
            val domain = "GITHUB.COM"
            val normalizedDomain = "github.com"
            every { userExcludedWebsiteRepository.save(any()) } throws DataIntegrityViolationException("duplicate")
            every { userExcludedWebsiteRepository.existsByUserIdAndDomain(ID, normalizedDomain) } returns true

            When("예외 도메인 추가를 요청하면") {
                Then("중복 예외가 발생한다.") {
                    shouldThrow<ExcludedDomainAlreadyExistsException> { userService.addMyExcludedDomain(ID, domain) }
                    verify(exactly = 1) { userExcludedWebsiteRepository.existsByUserIdAndDomain(ID, normalizedDomain) }
                }
            }
        }

        Given("저장 중 예상하지 못한 무결성 예외가 발생하면") {
            val domain = "github.com"
            every { userExcludedWebsiteRepository.save(any()) } throws DataIntegrityViolationException("unexpected")
            every { userExcludedWebsiteRepository.existsByUserIdAndDomain(ID, domain) } returns false

            When("예외 도메인 추가를 요청하면") {
                Then("원본 예외를 그대로 전달한다.") {
                    shouldThrow<DataIntegrityViolationException> { userService.addMyExcludedDomain(ID, domain) }
                }
            }
        }

        Given("사용자가 예외 도메인 삭제를 요청하면") {
            val domain = " GitHub.COM "
            val normalizedDomain = "github.com"
            every { userExcludedWebsiteRepository.deleteByUserIdAndDomain(ID, normalizedDomain) } returns 1L

            When("예외 도메인 삭제를 수행하면") {
                userService.deleteMyExcludedDomain(ID, domain)

                Then("정규화된 도메인으로 삭제가 수행된다.") {
                    verify(exactly = 1) {
                        userExcludedWebsiteRepository.deleteByUserIdAndDomain(ID, normalizedDomain)
                    }
                }
            }
        }
    }
}
