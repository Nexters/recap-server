package com.retoday.core.domain.recap.scheduler

import com.retoday.core.domain.recap.service.RecapService
import com.retoday.core.domain.user.entity.TimeZone
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.global.extension.getLogger
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class RecapScheduler(
    private val profileRepository: ProfileRepository,
    private val recapService: RecapService
) {
    private companion object {
        val logger = getLogger()
    }

    @Scheduled(cron = "0 */5 * * * *")
    fun createDailyRecapsAtUserMidnight() {
        val now = Instant.now()
        // 00시 00-04분에 해당하는 time zone
        val eligibleTimeZones =
            TimeZone.entries.filter { timeZone ->
                val userNow = now.atZone(timeZone.id)
                userNow.hour == 0 && userNow.minute < 5
            }
        if (eligibleTimeZones.isEmpty()) return

        val activeProfiles = profileRepository.findAllActiveByTimeZones(eligibleTimeZones)

        activeProfiles.forEach { profile ->
            val userNow = now.atZone(profile.timeZone.id)
            val recapDate = userNow.toLocalDate().minusDays(1)
            runCatching {
                recapService.createDailyRecap(profile.userId, recapDate)
            }.onFailure { throwable ->
                logger.error(throwable) {
                    "Failed to create daily recap. userId=${profile.userId}, recapDate=$recapDate, timeZone=${profile.timeZone}"
                }
            }
        }
    }
}
