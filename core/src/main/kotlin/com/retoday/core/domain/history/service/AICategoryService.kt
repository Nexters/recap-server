package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.client.AICategoryClient
import com.retoday.core.domain.history.entity.Category
import com.retoday.core.domain.history.exception.InvalidCategoryException
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class AICategoryService(
    private val websiteRepository: WebsiteRepository,
    private val categoryRepository: WebsiteCategoryRepository,
    private val aiClient: AICategoryClient
) {
    @Transactional
    fun classify(
        websiteId: Long,
        domain: String
    ) {
        val website =
            websiteRepository
                .findByIdOrNull(websiteId)
                ?.takeIf { it.categoryId == null }
                ?: return

        // ai 호출
        val predicted: Category = aiClient.classify(domain)

        val category =
            categoryRepository
                .findByName(predicted.displayName)
                ?: throw InvalidCategoryException()

        website.updateCategory(category.id!!)
    }
}
