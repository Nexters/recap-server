package com.retoday.core.domain.history.service

import com.retoday.core.domain.history.client.AICategoryClient
import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.exception.InvalidCategoryException
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
import com.retoday.core.domain.history.repository.WebsiteRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class AICategoryServiceTest :
    BehaviorSpec({
        val websiteRepository = mockk<WebsiteRepository>()
        val categoryRepository = mockk<WebsiteCategoryRepository>()
        val aiClient = mockk<AICategoryClient>()
        val aiCategoryService = AICategoryService(websiteRepository, categoryRepository, aiClient)

        Given("웹사이트 도메인 카테고리 분류가 필요할 때") {
            val websiteId = 1L
            val domain = "hackers.com"
            val website = Website(id = websiteId, domain = domain, categoryId = null)
            val studyCategory = WebsiteCategory(id = 10L, name = "학습")
            val categoryList = listOf(studyCategory)
            val categoryNames = listOf("학습")

            When("카테고리가 할당되지 않은 도메인인 경우") {
                every { websiteRepository.findByIdOrNull(websiteId) } returns website
                every { categoryRepository.findAll() } returns categoryList
                every { aiClient.classify(domain, categoryNames) } returns "학습"
                every { categoryRepository.findByName("학습") } returns studyCategory

                aiCategoryService.classify(websiteId, domain)

                Then("AI 결과에 따라 웹사이트의 카테고리가 업데이트되어야 한다") {
                    website.categoryId shouldBe 10L
                }
            }

            When("이미 카테고리가 할당되어 있는 웹사이트라면") {
                val alreadyClassifiedWebsite = Website(id = websiteId, domain = domain, categoryId = 20L)
                every { websiteRepository.findByIdOrNull(websiteId) } returns alreadyClassifiedWebsite

                aiCategoryService.classify(websiteId, domain)

                Then("AI를 호출하지 않고 조기 종료되어야 한다") {
                    verify(exactly = 0) { aiClient.classify(any(), any()) }
                }
            }

            When("AI가 분류한 카테고리가 DB에 존재하지 않는 이름이라면") {
                // 다시 null 상태인 객체 준비
                val freshWebsite = Website(id = websiteId, domain = domain, categoryId = null)
                every { websiteRepository.findByIdOrNull(websiteId) } returns freshWebsite
                every { categoryRepository.findAll() } returns categoryList
                every { aiClient.classify(domain, categoryNames) } returns "잘못된카테고리"
                every { categoryRepository.findByName("잘못된카테고리") } returns null

                Then("InvalidCategoryException이 발생해야 한다") {
                    shouldThrow<InvalidCategoryException> {
                        aiCategoryService.classify(websiteId, domain)
                    }
                }
            }
        }
    })
