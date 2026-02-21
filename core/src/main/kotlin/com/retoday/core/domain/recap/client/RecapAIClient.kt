package com.retoday.core.domain.recap.client

import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.UserActivityDto
import com.retoday.core.domain.recap.dto.UserTimelineDto

abstract class RecapAIClient(
    val provider: AIProvider
) {
    abstract val modelName: String

    abstract fun <T> generate(
        type: RecapType,
        nickname: String,
        activities: List<UserActivityDto>,
        responseClass: Class<T>
    ): T

    abstract fun <T> generateTimeline(
        type: RecapType,
        nickname: String,
        activities: List<UserTimelineDto>,
        responseClass: Class<T>
    ): T
}

enum class AIProvider {
    GEMINI
}
