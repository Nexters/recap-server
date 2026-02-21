package com.retoday.core.global.config

import com.google.genai.Client
import com.google.genai.types.GenerateContentConfig
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class GeminiConfiguration(
    @Value("\${gemini.api.key}") private val apiKey: String
) {
    @Bean
    fun geminiSdkClient(): Client = Client.builder().apiKey(apiKey).build()

    @Bean
    fun defaultAiConfig(): GenerateContentConfig =
        GenerateContentConfig
            .builder()
            .responseMimeType("application/json")
            .temperature(0.7f)
            .build()
}
