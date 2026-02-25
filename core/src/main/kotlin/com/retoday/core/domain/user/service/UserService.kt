package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.dto.result.GetMyProfileResult
import com.retoday.core.domain.user.entity.UserExcludedWebsiteDomain
import com.retoday.core.domain.user.exception.ExcludedDomainAlreadyExistsException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserExcludedWebsiteRepository
import org.springframework.cache.annotation.CacheEvict
import org.springframework.cache.annotation.Cacheable
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val profileRepository: ProfileRepository,
    private val userExcludedWebsiteRepository: UserExcludedWebsiteRepository
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

    @Cacheable(cacheNames = ["excluded-domains"], key = "#userId")
    @Transactional(readOnly = true)
    fun getExcludedDomains(userId: Long): List<String> =
        userExcludedWebsiteRepository
            .findAllByUserId(userId)
            .map { it.domain }

    @CacheEvict(cacheNames = ["excluded-domains"], key = "#userId")
    @Transactional
    fun addMyExcludedDomain(
        userId: Long,
        domain: String
    ) {
        val normalizedDomain = domain.trim().lowercase()

        try {
            userExcludedWebsiteRepository.save(
                UserExcludedWebsiteDomain(userId = userId, domain = normalizedDomain)
            )
        } catch (exception: DataIntegrityViolationException) {
            if (userExcludedWebsiteRepository.existsByUserIdAndDomain(userId, normalizedDomain)) {
                throw ExcludedDomainAlreadyExistsException(normalizedDomain)
            }
            throw exception
        }
    }
}
