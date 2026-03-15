package com.retoday.core.domain.history.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.fixture.createPage
import com.retoday.core.fixture.createWebsite
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

class CustomPageRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var pageRepository: PageRepository

    @Autowired
    private lateinit var websiteRepository: WebsiteRepository

    init {
        "getByUrlForShare()" {
            val website = websiteRepository.save(createWebsite(id = null, domain = "custom-page-test.com"))
            val saved =
                pageRepository.save(
                    createPage(
                        id = null,
                        websiteId = website.id,
                        url = "https://custom-page-test.com/path"
                    )
                )

            val found = pageRepository.getByUrlForShare("https://custom-page-test.com/path")

            found.id shouldBe saved.id
            found.websiteId shouldBe website.id
            found.url shouldBe "https://custom-page-test.com/path"
            found.title shouldBe saved.title
            found.description shouldBe saved.description
        }
    }
}
