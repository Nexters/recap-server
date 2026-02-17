package com.retoday.core.domain.recap.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.Client
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import com.retoday.core.domain.recap.component.RecapPromptManager
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.GeminiTopicResponse
import com.retoday.core.domain.recap.dto.UserActivityDto
import org.springframework.stereotype.Service

@Service
class GeminiRecapService(
    private val geminiClient: Client,
    private val defaultAiConfig: GenerateContentConfig,
    private val promptManager: RecapPromptManager,
    private val objectMapper: ObjectMapper
) {
    fun <T> askGemini(
        type: RecapType,
        nickname: String,
        activities: List<UserActivityDto>,
        responseClass: Class<T>
    ): T {
        val instruction = promptManager.getDailyRecapPrompt(type, mapOf("nickname" to nickname))
        val userDataJson = objectMapper.writeValueAsString(activities)

        val response =
            geminiClient.models.generateContent(
                "models/gemini-2.5-flash",
                "분석할 데이터: $userDataJson",
                GenerateContentConfig
                    .builder()
                    .systemInstruction(
                        Content
                            .builder()
                            .parts(listOf(Part.builder().text(instruction).build()))
                            .build()
                    ).responseMimeType("application/json")
                    .build()
            )

        val jsonString = response.text() ?: throw RuntimeException("${type.name} 응답 생성 실패")
        val cleanedJson = jsonString.replace("```json", "").replace("```", "").trim()

        return objectMapper.readValue(cleanedJson, responseClass)
    }

    // 1. Today's Recap
    fun generateRecap(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiRecapResponse = askGemini(RecapType.TODAY_RECAP, nickname, activities, GeminiRecapResponse::class.java)

    // 2. AI 타임라인
    fun generateTimeline(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiTimelineResponse = askGemini(RecapType.TIMELINE, nickname, activities, GeminiTimelineResponse::class.java)

    // 3. 많이 둘러본 주제
    fun generateTopics(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiTopicResponse = askGemini(RecapType.TOPIC, nickname, activities, GeminiTopicResponse::class.java)
}
