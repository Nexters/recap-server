package com.retoday.core.domain.history.client

interface AICategoryClient {
    fun classify(
        domain: String,
        categoryNames: List<String>
    ): String
}
