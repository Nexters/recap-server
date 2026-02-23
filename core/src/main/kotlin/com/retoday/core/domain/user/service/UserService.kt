package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.dto.result.GetMyProfileResult
import com.retoday.core.domain.user.entity.UserExcludedWebsiteDomain
import com.retoday.core.domain.user.exception.ExcludedDomainAlreadyExistsException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserExcludedWebsiteRepository
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val profileRepository: ProfileRepository,
    private val userExcludedWebsiteRepository: UserExcludedWebsiteRepository,
    private val excludedDomainCacheService: ExcludedDomainCacheService
) {
    @Transactional(readOnly = true)
    fun getMyProfile(userId: Long): GetMyProfileResult {
        val profileWithEmail = profileRepository.findByUserIdWithEmail(userId)!!
        val excludedDomains =
            userExcludedWebsiteRepository
                .findAllByUserId(userId)
                .map { it.domain }

        return GetMyProfileResult.of(profileWithEmail, excludedDomains)
    }

    @Transactional
    fun addMyExcludedDomain(
        userId: Long,
        domain: String
    ) {
        val normalizedDomain =
            domain
                .trim()
                .lowercase()

        try {
            userExcludedWebsiteRepository.save(
                UserExcludedWebsiteDomain(
                    userId = userId,
                    domain = normalizedDomain
                )
            )
        } catch (exception: DataIntegrityViolationException) {
            if (userExcludedWebsiteRepository.existsByUserIdAndDomain(userId, normalizedDomain)) {
                throw ExcludedDomainAlreadyExistsException(normalizedDomain)
            }

            throw exception
        }

        // TODO: 추후 예외도메인 삭제 API에도 캐시 무효화 필요
        excludedDomainCacheService.invalidate(userId)
    }
}
