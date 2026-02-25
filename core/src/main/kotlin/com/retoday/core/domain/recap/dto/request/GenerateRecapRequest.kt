package com.retoday.core.domain.recap.dto.request

import com.retoday.core.domain.recap.component.RecapType

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
    val payload: RecapPayload
)
