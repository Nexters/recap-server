package com.retoday.core.domain.history.event

import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class WebsiteCategoryClassificationEventListener(
    private val websiteRepository: WebsiteRepository
) {
    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun handleClassificationEvent(event: WebsiteCategoryClassificationEvent) {
        websiteRepository
            .findByIdOrNull(event.websiteId)
            ?.takeIf { it.categoryId == null }
            ?: return

        // TODO: AI 분류 요청
    }
}
