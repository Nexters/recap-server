package com.retoday.core.domain.recap.dto.request

import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.user.entity.Language

sealed interface RecapPayload {
    data class Activities(
        val items: List<UserActivityRequest>
    ) : RecapPayload

    data class Timelines(
        val items: List<UserTimelineRequest>
    ) : RecapPayload
}

data class GenerateRecapRequest(
    val type: RecapType,
    val nickname: String,
    val language: Language,
    val payload: RecapPayload
)
