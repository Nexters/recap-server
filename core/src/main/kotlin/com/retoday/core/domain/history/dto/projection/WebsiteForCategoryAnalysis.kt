package com.retoday.core.domain.history.dto.projection

interface WebsiteForCategoryAnalysis {
    val domain: String
    val faviconUrl: String?
    val categoryName: String?
    val stayDuration: Long
}
