package com.retoday.core.domain.recap.scheduler

import com.retoday.core.domain.recap.service.RecapService
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.domain.user.repository.UserRepository
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RecapScheduler(
    private val userRepository: UserRepository,
    private val profileRepository: ProfileRepository,
    private val recapService: RecapService
) {
    @Scheduled(cron = "0 */5 * * * *")
    fun createDailyRecapsAtUserMidnight() {
        val now = Instant.now()
        val activeUsers = userRepository.findAllByIsActiveTrue()

        activeUsers.forEach { user ->
            val profile = profileRepository.findByUserId(user.id!!) ?: return@forEach
            val userNow = now.atZone(profile.timeZone.id)

            if (userNow.hour != 0 || userNow.minute >= 5) return@forEach

            val recapDate = userNow.toLocalDate().minusDays(1)
            runCatching {
                recapService.createDailyRecap(user.id!!, recapDate)
            }
        }
    }
}
