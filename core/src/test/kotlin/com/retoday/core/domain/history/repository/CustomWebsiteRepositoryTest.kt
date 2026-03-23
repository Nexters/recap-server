package com.retoday.core.domain.history.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.fixture.createWebsite
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired

class CustomWebsiteRepositoryTest : RepositoryTest() {
    @Autowired
    private lateinit var websiteRepository: WebsiteRepository

    init {
        "getByDomainForShare()" {
            val saved = websiteRepository.save(createWebsite(id = null, domain = "custom-website-test.com"))

            val found = websiteRepository.getByDomainForShare("custom-website-test.com")

            found.id shouldBe saved.id
            found.domain shouldBe "custom-website-test.com"
            found.faviconUrl shouldBe saved.faviconUrl
        }

        "upsertByDomain()" {
            val domain = "custom-website-upsert.com"

            websiteRepository.upsertByDomain(createWebsite(id = null, domain = domain, faviconUrl = null))
            websiteRepository.upsertByDomain(
                createWebsite(
                    id = null,
                    domain = domain,
                    faviconUrl = "https://custom-website-upsert.com/favicon.ico"
                )
            )

            val found = websiteRepository.getByDomainForShare(domain)

            found.domain shouldBe domain
            found.faviconUrl shouldBe "https://custom-website-upsert.com/favicon.ico"
        }
    }
}
