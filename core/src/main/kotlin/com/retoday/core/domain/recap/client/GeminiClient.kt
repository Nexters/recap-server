package com.retoday.core.domain.recap.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import com.retoday.core.domain.recap.component.RecapPromptManager
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.request.GenerateRecapRequest
import com.retoday.core.domain.recap.dto.request.RecapPayload
import com.retoday.core.domain.recap.exception.RecapResponseEmptyException
import com.retoday.core.domain.user.entity.Language
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

    override fun <T> generate(
        request: GenerateRecapRequest,
        responseClass: Class<T>
    ): T = executeGeneration(request, responseClass)

    private fun <T> executeGeneration(
        request: GenerateRecapRequest,
        responseClass: Class<T>
    ): T {
        request.validatePayloadType()
        val instruction =
            promptManager.getDailyRecapPrompt(
                request.type,
                mapOf(
                    "nickname" to request.nickname,
                    "language" to request.language.toPromptLanguage()
                )
            )
        val userDataJson =
            objectMapper.writeValueAsString(
                when (val payload = request.payload) {
                    is RecapPayload.Activities -> payload.items
                    is RecapPayload.Timelines -> payload.items
                }
            )

        val response =
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

        val jsonString = response.text() ?: throw RecapResponseEmptyException()

        return objectMapper.readValue(jsonString, responseClass)
    }

    private fun GenerateRecapRequest.validatePayloadType() {
        when (type) {
            RecapType.TIMELINE -> require(payload is RecapPayload.Timelines)
            RecapType.TODAY_RECAP,
            RecapType.TOPIC -> require(payload is RecapPayload.Activities)
        }
    }

    private fun Language.toPromptLanguage(): String =
        when (this) {
            Language.KO -> "Korean"
            Language.EN -> "English"
            Language.JA -> "Japanese"
        }
}
