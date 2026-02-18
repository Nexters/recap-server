package com.retoday.core.domain.recap.service

import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.GeminiTopicResponse
import com.retoday.core.domain.recap.dto.UserActivityDto
import org.springframework.stereotype.Service

@Service
class RecapService(
    private val recapAIClient: RecapAIClient
) {
    fun generateRecap(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiRecapResponse =
        recapAIClient.generate(
            RecapType.TODAY_RECAP,
            nickname,
            activities,
            GeminiRecapResponse::class.java
        )

    fun generateTimeline(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiTimelineResponse =
        recapAIClient.generate(
            RecapType.TIMELINE,
            nickname,
            activities,
            GeminiTimelineResponse::class.java
        )

    fun generateTopics(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiTopicResponse =
        recapAIClient.generate(
            RecapType.TOPIC,
            nickname,
            activities,
            GeminiTopicResponse::class.java
        )
}
