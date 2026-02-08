package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.dto.result.GetProfileByUserIdResult
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.website.repository.WebsiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val profileRepository: ProfileRepository,
    private val websiteRepository: WebsiteRepository
) {
    @Transactional(readOnly = true)
    fun getProfileByUserId(userId: Long): GetProfileByUserIdResult {
        val profileWithEmail = profileRepository.findByUserIdWithEmail(userId)!!
        val excludedDomains = websiteRepository.findAllExcludedDomainsByUserId(userId)

        return GetProfileByUserIdResult.of(profileWithEmail, excludedDomains)
    }
}
