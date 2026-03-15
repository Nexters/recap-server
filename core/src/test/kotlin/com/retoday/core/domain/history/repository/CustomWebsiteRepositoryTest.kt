package com.retoday.core.domain.history.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.fixture.createWebsite
import io.kotest.matchers.shouldBe
import org.springframework.beans.factory.annotation.Autowired
import java.time.Instant

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

            websiteRepository.upsertByDomain(
                website = createWebsite(id = null, domain = domain, faviconUrl = null),
                createdAt = Instant.now()
            )
            websiteRepository.upsertByDomain(
                website = createWebsite(id = null, domain = domain, faviconUrl = "https://custom-website-upsert.com/favicon.ico"),
                createdAt = Instant.now()
            )

            val found = websiteRepository.getByDomainForShare(domain)

            found.domain shouldBe domain
            found.faviconUrl shouldBe "https://custom-website-upsert.com/favicon.ico"
        }
    }
}
