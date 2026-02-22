package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.dto.result.GetMyProfileResult
import com.retoday.core.domain.user.entity.UserExcludedWebsiteDomain
import com.retoday.core.domain.user.exception.ExcludedDomainAlreadyExistsException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserExcludedWebsiteRepository
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

    @Transactional
    fun addMyExcludedDomain(
        userId: Long,
        domain: String
    ) {
        if (userExcludedWebsiteRepository.existsByUserIdAndDomain(userId, domain)) {
            throw ExcludedDomainAlreadyExistsException(domain)
        }

        userExcludedWebsiteRepository.save(
            UserExcludedWebsiteDomain(
                userId = userId,
                domain = domain
            )
        )
    }
}
