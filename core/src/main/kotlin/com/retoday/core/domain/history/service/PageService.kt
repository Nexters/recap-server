package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.entity.Page
import com.retoday.core.domain.history.repository.PageRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PageService(
    private val pageRepository: PageRepository
) {
    @Transactional
    fun findOrCreate(
        websiteId: Long,
        url: String,
        title: String?,
        description: String?
    ): Page {
        pageRepository.upsertByUrl(
            Page(
                websiteId = websiteId,
                url = url,
                title = title,
                description = description
            )
        )
        return pageRepository.getByUrlForShare(url)
    }
}
