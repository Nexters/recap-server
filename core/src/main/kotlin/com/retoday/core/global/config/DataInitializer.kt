package com.retoday.core.global.config

import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInitializer(
    private val websiteCategoryRepository: WebsiteCategoryRepository
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments) {
        initializeWebsiteCategories()
    }

    private fun initializeWebsiteCategories() {
        if (websiteCategoryRepository.count() > 0) {
            return
        }

        val categories =
            listOf(
                "학습",
                "쇼핑",
                "게임",
                "콘텐츠",
                "커뮤니티",
                "뉴스/시사",
                "금융/자산",
                "생활/편의",
                "웹서핑",
                "디자인",
                "개발",
                "기타"
            )

        websiteCategoryRepository.saveAll(
            categories.map { WebsiteCategory(name = it) }
        )
    }
}
