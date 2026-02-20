package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.WebsiteCategory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteCategoryRepository : JpaRepository<WebsiteCategory, Long> {
    fun findByName(name: String): WebsiteCategory?
}
