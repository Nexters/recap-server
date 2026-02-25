package com.retoday.core.domain.history.client

interface AICategoryClient {
    fun classify(
        domain: String,
        categoryCodes: List<String>
    ): String
}
