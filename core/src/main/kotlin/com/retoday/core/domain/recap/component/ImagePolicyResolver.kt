package com.retoday.core.domain.recap.component

import com.retoday.core.domain.history.entity.WebsiteCategoryCode
import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.entity.RecapImage
import com.retoday.core.domain.recap.properties.RecapImageProperties
import org.springframework.stereotype.Component
import java.time.Instant
import java.time.ZoneId
import kotlin.random.Random

@Component
class ImagePolicyResolver(
    private val recapImageProperties: RecapImageProperties
) {
    fun resolveImageUrl(
        userId: Long,
        firstVisitedAt: Instant,
        zoneId: ZoneId,
        topCategoryName: String?,
        categoryCount: Int,
        activities: List<UserActivityProjection>
    ): String {
        val recapImage =
            resolvePolicy(
                userId = userId,
                firstVisitedAt = firstVisitedAt,
                zoneId = zoneId,
                topCategoryName = topCategoryName,
                categoryCount = categoryCount,
                activities = activities
            )
        return "${recapImageProperties.baseUrl.trimEnd('/')}/${recapImage.fileName}"
    }

    private fun resolvePolicy(
        userId: Long,
        firstVisitedAt: Instant,
        zoneId: ZoneId,
        topCategoryName: String?,
        categoryCount: Int,
        activities: List<UserActivityProjection>
    ): RecapImage {
        normalizeCategory(topCategoryName)?.let { topCategory ->
            mapRecapImage(topCategory)?.let { return it }
        }

        val totalDurationMinutes = activities.sumOf { it.stayDuration.coerceAtLeast(0) }
        if (totalDurationMinutes >= 12 * 60) return RecapImage.SCREEN_TIME_OVER_12H
        if (totalDurationMinutes < 60) return RecapImage.SCREEN_TIME_UNDER_1H

        if (categoryCount >= 5) return RecapImage.CATEGORY_OVER_5
        if (categoryCount == 1) return RecapImage.CATEGORY_ONLY_1

        val startedHour = firstVisitedAt.atZone(zoneId).hour
        if (startedHour >= 21) return RecapImage.START_AFTER_9PM
        if (startedHour < 9) return RecapImage.START_BEFORE_9AM

        return listOf(
            RecapImage.RANDOM_1,
            RecapImage.RANDOM_2,
            RecapImage.RANDOM_3
        )[Random(userId xor firstVisitedAt.epochSecond).nextInt(3)]
    }

    private fun normalizeCategory(raw: String?): WebsiteCategoryCode? = WebsiteCategoryCode.fromLabel(raw)

    private fun mapRecapImage(category: WebsiteCategoryCode): RecapImage? =
        when (category) {
            WebsiteCategoryCode.STUDY -> RecapImage.STUDY
            WebsiteCategoryCode.SHOPPING -> RecapImage.SHOPPING
            WebsiteCategoryCode.GAMING -> RecapImage.GAME
            WebsiteCategoryCode.CONTENT -> RecapImage.CONTENT
            WebsiteCategoryCode.COMMUNITY -> RecapImage.COMMUNITY
            WebsiteCategoryCode.NEWS -> RecapImage.NEWS
            WebsiteCategoryCode.FINANCE -> RecapImage.FINANCE
            WebsiteCategoryCode.LIFESTYLE -> RecapImage.LIFE
            WebsiteCategoryCode.BROWSING -> RecapImage.SURFING
            WebsiteCategoryCode.DESIGN -> RecapImage.DESIGN
            WebsiteCategoryCode.AI -> RecapImage.AI
            WebsiteCategoryCode.DEVELOPMENT -> RecapImage.DEVELOPMENT
            WebsiteCategoryCode.ETC -> null
        }
}
