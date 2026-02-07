package com.retoday.core.domain.user.service

import com.retoday.core.domain.user.dto.result.GetProfileByUserIdResult
import com.retoday.core.domain.user.exception.UserNotFoundException
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository
) {
    @Transactional(readOnly = true)
    fun getProfileByUserId(userId: Long): GetProfileByUserIdResult =
        profileRepository
            .findByUserIdWithEmailAndExcludedDomains(userId)
            ?.let { GetProfileByUserIdResult.from(it) }
            ?: throw UserNotFoundException()
}
