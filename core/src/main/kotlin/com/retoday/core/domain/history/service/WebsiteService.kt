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
    @Transactional
    fun findOrCreate(domain: String, faviconUrl: String?): Website =
        websiteRepository.findByDomain(domain) ?: run {
            Website(
                domain = domain,
                categoryId = null,
                faviconUrl = faviconUrl
            ).let { websiteRepository.save(it) }
                .also {
                    eventPublisher.publishEvent(
                        WebsiteCategoryClassificationEvent(it.id!!, domain)
                    )
                }
        }
}
