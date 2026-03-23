package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.event.WebsiteCategoryClassificationEvent
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WebsiteService(
    private val websiteRepository: WebsiteRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    fun save(website: Website): Website = websiteRepository.save(website)

    @Transactional
    fun findOrCreate(
        domain: String,
        faviconUrl: String?
    ): Website {
        val rows = websiteRepository.upsertByDomain(Website(domain = domain, faviconUrl = faviconUrl))
        val website = websiteRepository.getByDomainForShare(domain)
        if (rows == 1) {
            eventPublisher.publishEvent(WebsiteCategoryClassificationEvent(website.id, domain))
        }
        return website
    }
}
