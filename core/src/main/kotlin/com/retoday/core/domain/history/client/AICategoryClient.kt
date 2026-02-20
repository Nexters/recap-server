package com.retoday.core.domain.history.client

import com.retoday.core.domain.history.entity.Category

interface AICategoryClient {
    fun classify(domain: String): Category
}
