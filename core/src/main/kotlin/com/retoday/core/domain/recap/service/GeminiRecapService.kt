package com.retoday.core.domain.recap.service

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import com.retoday.core.domain.recap.component.RecapPromptManager
import com.retoday.core.domain.recap.dto.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.UserActivityDto
import org.springframework.stereotype.Service

@Service
class GeminiRecapService(
    private val geminiClient: Client,
    private val defaultAiConfig: GenerateContentConfig,
    private val promptManager: RecapPromptManager,
    private val objectMapper: ObjectMapper
) {
    fun generateRecap(
        nickname: String,
        activities: List<UserActivityDto>
    ): GeminiRecapResponse {
        val userDataJson = objectMapper.writeValueAsString(activities)
        val variables =
            mapOf(
                "nickname" to nickname,
                "activities" to userDataJson
            )
        val userMessage = "사용자 닉네임: $nickname\n활동 기록: $userDataJson"

        val systemMessage =
            promptManager.getDailyRecapPrompt(
                type = com.retoday.core.domain.recap.component.RecapType.TODAY_RECAP,
                variables = variables
            )
        val response =
            geminiClient.models.generateContent(
                "models/gemini-2.5-flash",
                "$systemMessage\n\n$userMessage",
                defaultAiConfig
            )

        val jsonString = response.text() ?: throw RuntimeException("AI 응답 생성에 실패했습니다.")

        val cleanedJson = jsonString.replace("```json", "").replace("```", "").trim()

        return objectMapper.readValue(cleanedJson, GeminiRecapResponse::class.java)
    }
}
