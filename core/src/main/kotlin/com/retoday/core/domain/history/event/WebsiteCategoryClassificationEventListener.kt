package com.retoday.core.domain.history.event

import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.context.event.EventListener
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Component
class WebsiteCategoryClassificationEventListener(
    private val websiteRepository: WebsiteRepository
) {

    @Async
    @EventListener
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleClassificationEvent(event: WebsiteCategoryClassificationEvent) {
        websiteRepository.findByIdOrNull(event.websiteId)
            ?.takeIf { it.categoryId == null }
            ?: return

        // TODO: AI 분류 요청
    }
}
