package com.retoday.core.domain.user.service

import com.retoday.core.domain.history.repository.WebsiteRepository
import com.retoday.core.domain.user.dto.result.GetMyProfileResult
import com.retoday.core.domain.user.repository.ProfileRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.ZoneId

@Service
class UserService(
    private val profileRepository: ProfileRepository,
    private val websiteRepository: WebsiteRepository
) {
    @Transactional(readOnly = true)
    fun getMyProfile(userId: Long): GetMyProfileResult {
        val profileWithEmail = profileRepository.findByUserIdWithEmail(userId)!!
        val excludedDomains = websiteRepository.findAllExcludedDomainsByUserId(userId)

        return GetMyProfileResult.of(profileWithEmail, excludedDomains)
    }

    @Transactional(readOnly = true)
    fun getUserTimeZone(userId: Long): ZoneId {
        val profile = profileRepository.findByUserId(userId)!!

        return profile.timeZone.id
    }
}
