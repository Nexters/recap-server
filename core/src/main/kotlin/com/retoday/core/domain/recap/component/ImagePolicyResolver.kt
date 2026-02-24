package com.retoday.core.domain.recap.component

import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
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
        val fileName =
            ImagePolicyMapper.toImageFileName(
                resolvePolicy(
                    userId = userId,
                    firstVisitedAt = firstVisitedAt,
                    zoneId = zoneId,
                    topCategoryName = topCategoryName,
                    categoryCount = categoryCount,
                    activities = activities
                )
            )
        return "${recapImageProperties.baseUrl.trimEnd('/')}/$fileName"
    }

    private fun resolvePolicy(
        userId: Long,
        firstVisitedAt: Instant,
        zoneId: ZoneId,
        topCategoryName: String?,
        categoryCount: Int,
        activities: List<UserActivityProjection>
    ): ImagePolicyType {
        normalizeCategory(topCategoryName)?.let { topCategory ->
            return when (topCategory) {
                Category.LEARNING -> ImagePolicyType.IMAGE_01_LEARNING_TOP
                Category.SHOPPING -> ImagePolicyType.IMAGE_02_SHOPPING_TOP
                Category.GAME -> ImagePolicyType.IMAGE_03_GAME_TOP
                Category.CONTENT -> ImagePolicyType.IMAGE_04_CONTENT_TOP
                Category.COMMUNITY -> ImagePolicyType.IMAGE_05_COMMUNITY_TOP
                Category.NEWS -> ImagePolicyType.IMAGE_06_NEWS_TOP
                Category.FINANCE -> ImagePolicyType.IMAGE_07_FINANCE_TOP
                Category.LIFE -> ImagePolicyType.IMAGE_08_LIFE_TOP
                Category.SURFING -> ImagePolicyType.IMAGE_09_SURFING_TOP
                Category.DESIGN -> ImagePolicyType.IMAGE_10_DESIGN_TOP
                Category.DEVELOPMENT -> ImagePolicyType.IMAGE_11_DEVELOPMENT_TOP
            }
        }

        val totalDurationMinutes = activities.sumOf { it.stayDuration.coerceAtLeast(0) }
        if (totalDurationMinutes >= 12 * 60) return ImagePolicyType.IMAGE_12_SCREEN_TIME_OVER_12H
        if (totalDurationMinutes < 60) return ImagePolicyType.IMAGE_13_SCREEN_TIME_UNDER_1H

        if (categoryCount >= 5) return ImagePolicyType.IMAGE_14_CATEGORY_OVER_5
        if (categoryCount == 1) return ImagePolicyType.IMAGE_15_CATEGORY_ONLY_1

        val startedHour = firstVisitedAt.atZone(zoneId).hour
        if (startedHour >= 21) return ImagePolicyType.IMAGE_16_START_AFTER_9PM
        if (startedHour < 9) return ImagePolicyType.IMAGE_17_START_BEFORE_9AM

        return listOf(
            ImagePolicyType.IMAGE_18_RANDOM_1,
            ImagePolicyType.IMAGE_19_RANDOM_2,
            ImagePolicyType.IMAGE_20_RANDOM_3
        )[Random(userId xor firstVisitedAt.epochSecond).nextInt(3)]
    }

    private fun normalizeCategory(raw: String?): Category? =
        when (raw?.trim()?.uppercase()) {
            "학습" -> Category.LEARNING
            "쇼핑" -> Category.SHOPPING
            "게임" -> Category.GAME
            "콘텐츠" -> Category.CONTENT
            "커뮤니티" -> Category.COMMUNITY
            "뉴스/시사" -> Category.NEWS
            "금융/자산" -> Category.FINANCE
            "생활/편의" -> Category.LIFE
            "웹서핑" -> Category.SURFING
            "디자인" -> Category.DESIGN
            "개발" -> Category.DEVELOPMENT
            else -> null
        }

    private enum class Category {
        LEARNING,
        SHOPPING,
        GAME,
        CONTENT,
        COMMUNITY,
        NEWS,
        FINANCE,
        LIFE,
        SURFING,
        DESIGN,
        DEVELOPMENT
    }
}
