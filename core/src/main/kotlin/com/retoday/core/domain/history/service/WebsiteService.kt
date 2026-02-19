package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.event.WebsiteCategoryClassificationEvent
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class WebsiteService(
    private val websiteRepository: WebsiteRepository,
    private val eventPublisher: ApplicationEventPublisher
) {
    @Transactional
    fun findOrCreate(
        domain: String,
        faviconUrl: String?
    ): Website =
        websiteRepository.findByDomain(domain) ?: try {
            websiteRepository
                .save(Website(domain = domain, faviconUrl = faviconUrl))
                .also { eventPublisher.publishEvent(WebsiteCategoryClassificationEvent(it.id!!, domain)) }
        } catch (e: DataIntegrityViolationException) {
            websiteRepository.findByDomain(domain)!!
        }
}
