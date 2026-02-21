package com.retoday.core.domain.recap.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import com.retoday.core.domain.recap.component.RecapPromptManager
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.UserActivityDto
import com.retoday.core.domain.recap.dto.UserTimelineDto
import com.retoday.core.domain.recap.exception.RecapGenerationException
import com.retoday.core.domain.recap.exception.RecapParsingException
import com.retoday.core.domain.recap.exception.RecapResponseEmptyException
import com.retoday.core.global.annotation.Client
import org.springframework.beans.factory.annotation.Value
import com.google.genai.Client as GeminiClient

@Client
class GeminiClient(
    @Value("\${gemini.api.model}") private val modelVersion: String,
    private val geminiSdkClient: GeminiClient,
    private val promptManager: RecapPromptManager,
    private val objectMapper: ObjectMapper
) : RecapAIClient(provider = AIProvider.GEMINI) {
    private companion object {
        const val MODEL_PREFIX = "models/"
        const val RESPONSE_MIME_TYPE = "application/json"
    }

    override val modelName: String = modelVersion

    // 기본 활동 데이터용
    override fun <T> generate(
        type: RecapType,
        nickname: String,
        activities: List<UserActivityDto>,
        responseClass: Class<T>
    ): T = executeGeneration(type, nickname, activities, responseClass)

    // 타임라인 데이터용
    override fun <T> generateTimeline(
        type: RecapType,
        nickname: String,
        activities: List<UserTimelineDto>,
        responseClass: Class<T>
    ): T = executeGeneration(type, nickname, activities, responseClass)

    // 공통
    private fun <T, D> executeGeneration(
        type: RecapType,
        nickname: String,
        dataList: List<D>,
        responseClass: Class<T>
    ): T {
        val instruction = promptManager.getDailyRecapPrompt(type, mapOf("nickname" to nickname))
        val userDataJson = objectMapper.writeValueAsString(dataList)

        val response =
            try {
                geminiSdkClient.models.generateContent(
                    MODEL_PREFIX + modelVersion,
                    "분석할 데이터: $userDataJson",
                    GenerateContentConfig
                        .builder()
                        .systemInstruction(
                            Content
                                .builder()
                                .parts(listOf(Part.builder().text(instruction).build()))
                                .build()
                        ).responseMimeType(RESPONSE_MIME_TYPE)
                        .build()
                )
            } catch (e: Exception) {
                throw RecapGenerationException()
            }

        val jsonString = response.text() ?: throw RecapResponseEmptyException()
        val cleanedJson = jsonString.replace("```json", "").replace("```", "").trim()

        return try {
            objectMapper.readValue(cleanedJson, responseClass)
        } catch (e: Exception) {
            throw RecapParsingException()
        }
    }
}
