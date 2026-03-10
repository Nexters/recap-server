package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.entity.WebsiteCategoryCode
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteCategoryRepository : ListCrudRepository<WebsiteCategory, Long> {
    fun findByCode(code: WebsiteCategoryCode): WebsiteCategory?
}
