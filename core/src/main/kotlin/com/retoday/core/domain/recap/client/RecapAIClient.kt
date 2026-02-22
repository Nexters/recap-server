package com.retoday.core.domain.recap.client

import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.request.GenerateRecapRequest

abstract class RecapAIClient(
    val provider: AIProvider
) {
    abstract val modelName: String

    abstract fun <T> generate(
        request: GenerateRecapRequest,
        responseClass: Class<T>
    ): T
}

enum class AIProvider {
    GEMINI
}
