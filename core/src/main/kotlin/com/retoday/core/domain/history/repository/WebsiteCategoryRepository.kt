package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.entity.WebsiteCategoryCode
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteCategoryRepository : JpaRepository<WebsiteCategory, Long> {
    fun findByCode(code: WebsiteCategoryCode): WebsiteCategory?
}
