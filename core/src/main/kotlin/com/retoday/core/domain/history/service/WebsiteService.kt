package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.event.WebsiteCategoryClassificationEvent
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.relational.core.conversion.DbActionExecutionException
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
    ): Website =
        websiteRepository
            .findByDomain(domain)
            ?.let { website ->
                if (website.faviconUrl == null && faviconUrl != null) {
                    website.updateFaviconUrl(faviconUrl)
                    websiteRepository.save(website)
                } else {
                    website
                }
            } ?: try {
            websiteRepository
                .save(Website(domain = domain, faviconUrl = faviconUrl))
                .also { eventPublisher.publishEvent(WebsiteCategoryClassificationEvent(it.id, domain)) }
        } catch (e: DbActionExecutionException) {
            if (!e.isDuplicateKeyViolation()) throw e

            websiteRepository.getByDomainForShare(domain)
        }
}
