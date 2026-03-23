package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Page
import org.springframework.data.repository.ListCrudRepository

interface PageRepository : ListCrudRepository<Page, Long>, CustomPageRepository {
    fun findByUrl(url: String): Page?
}
