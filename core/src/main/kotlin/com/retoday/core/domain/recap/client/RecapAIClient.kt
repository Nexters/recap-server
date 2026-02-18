package com.retoday.core.domain.recap.client

import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.UserActivityDto

abstract class RecapAIClient(
    val provider: AIProvider
) {
    abstract fun <T> generate(
        type: RecapType,
        nickname: String,
        activities: List<UserActivityDto>,
        responseClass: Class<T>
    ): T
}

enum class AIProvider {
    GEMINI
}
