package com.retoday.core.domain.history.event

import com.retoday.core.domain.history.repository.WebsiteRepository
import com.retoday.core.domain.history.service.HistoryService
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class WebsiteCategoryClassificationEventListener(
    private val websiteRepository: WebsiteRepository,
    private val historyService: HistoryService
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleClassificationEvent(event: WebsiteCategoryClassificationEvent) {
        val website =
            websiteRepository
                .findByIdOrNull(event.websiteId)
                ?.takeIf { it.categoryId == null }
                ?: return

        historyService.classifyCategory(website, event.domain)
    }
}
