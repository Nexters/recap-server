package com.retoday.core.domain.history.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
import com.retoday.core.domain.history.entity.Category
import com.retoday.core.global.annotation.Client
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.io.Resource
import java.nio.charset.StandardCharsets
import com.google.genai.Client as GeminiSdkClient

@Client
class GeminiCategoryClient(
    @Value("\${gemini.api.model}") private val modelVersion: String,
    @Value("classpath:prompts/ai-category.md") private val promptResource: Resource,
    private val geminiSdkClient: GeminiSdkClient,
    private val objectMapper: ObjectMapper
) : AICategoryClient {
    private companion object {
        const val MODEL_PREFIX = "models/"
        const val RESPONSE_MIME_TYPE = "application/json"
    }

    override fun classify(domain: String): Category {
        val instruction = loadInstruction()

        val response =
            geminiSdkClient.models.generateContent(
                MODEL_PREFIX + modelVersion,
                "도메인: $domain",
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

        val jsonString = response.text() ?: throw RuntimeException("AI 응답이 비어있습니다.")

        return try {
            val node = objectMapper.readTree(jsonString)
            val categoryName = node.get("category").asText()

            Category.fromDisplayName(categoryName) ?: Category.ETC
        } catch (e: Exception) {
            Category.ETC
        }
    }

    // 프롬프트 읽어오고 카테고리 목록 주입
    private fun loadInstruction(): String {
        val rawPrompt =
            promptResource.inputStream.use {
                String(it.readAllBytes(), StandardCharsets.UTF_8)
            }
        val categoriesString = Category.entries.joinToString(separator = ", ") { it.displayName }

        return rawPrompt.replace("{categories}", "[$categoriesString]")
    }
}
