package com.retoday.core.domain.recap.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "recap.image")
data class RecapImageProperties(
    val baseUrl: String
)
