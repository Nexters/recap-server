package com.retoday.core.global.alert

import com.retoday.core.global.extension.getLogger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClient

@Service
class DiscordAlertService(
    @Value("\${discord.alert.webhook-url:}") private val webhookUrl: String,
    private val restClient: RestClient
) {
    private companion object {
        val logger = getLogger()
    }

    fun send(message: String) {
        if (webhookUrl.isBlank()) return

        runCatching {
            restClient
                .post()
                .uri(webhookUrl)
                .body(mapOf("content" to message))
                .retrieve()
                .toBodilessEntity()
        }.onFailure { e ->
            logger.error(e) { "Failed to send Discord alert" }
        }
    }
}
