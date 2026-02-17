package com.retoday.core.domain.history.repository

import com.retoday.core.domain.history.entity.Page
import org.springframework.data.jpa.repository.JpaRepository

interface PageRepository : JpaRepository<Page, Long> {
    fun findByUrl(url: String): Page?
}
