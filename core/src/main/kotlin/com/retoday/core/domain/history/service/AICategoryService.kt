package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.client.AICategoryClient
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

        val categories =
            categoryRepository
                .findAll()
                .map { it.name }

        val predictedName =
            aiClient.classify(domain, categories)

        val category =
            categoryRepository
                .findByName(predictedName)
                ?: throw InvalidCategoryException()

        website.updateCategory(category.id!!)
    }
}
