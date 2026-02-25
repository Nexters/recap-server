package com.retoday.core.domain.history.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.google.genai.types.Content
import com.google.genai.types.GenerateContentConfig
import com.google.genai.types.Part
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
    override fun classify(
        domain: String,
        categoryCodes: List<String>
    ): String {
        val instruction = loadInstruction(categoryCodes)

        val response =
            geminiSdkClient.models.generateContent(
                "models/$modelVersion",
                "도메인: $domain",
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

        val jsonString =
            response.text()
                ?: throw RuntimeException("AI 응답이 비어있습니다.")

        val node = objectMapper.readTree(jsonString)
        return node.get("category")?.asText() ?: ""
    }

    // 프롬프트와 카테고리 코드 전달
    private fun loadInstruction(categoryCodes: List<String>): String {
        val rawPrompt =
            promptResource.inputStream.use {
                String(it.readAllBytes(), StandardCharsets.UTF_8)
            }

        val categoriesString = categoryCodes.joinToString(", ")

        return rawPrompt.replace("{categories}", "[$categoriesString]")
    }
}
