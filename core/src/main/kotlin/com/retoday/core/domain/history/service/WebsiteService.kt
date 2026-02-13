package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WebsiteService(
    private val websiteRepository: WebsiteRepository
) {
    @Transactional
    fun findOrCreate(
        domain: String,
        faviconUrl: String?
    ) = websiteRepository.findByDomain(domain)
        ?: websiteRepository.save(
            Website(
                domain = domain,
                categoryId = null,
                faviconUrl = faviconUrl
            )
        )
}
