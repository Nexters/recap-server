package com.retoday.core.domain.history.repository

import com.retoday.core.common.RepositoryTest
import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.user.entity.User
import com.retoday.core.fixture.createUser
import com.retoday.core.fixture.createUserExcludedWebsite
import com.retoday.core.fixture.createWebsite
import io.kotest.core.test.TestCase
import io.kotest.matchers.collections.shouldContainExactlyInAnyOrder
import org.springframework.beans.factory.annotation.Autowired

class CustomWebsiteRepositoryTest : RepositoryTest() {
    private companion object {
        val DOMAINS = listOf("re-today.com", "github.com", "google.com")
    }

    @Autowired
    private lateinit var customWebsiteRepository: CustomWebsiteRepositoryImpl
    private lateinit var user: User
    private lateinit var websites: List<Website>

    override suspend fun beforeEach(testCase: TestCase) {
        user =
            createUser(id = null)
                .save()
        websites =
            DOMAINS.map {
                createWebsite(id = null, domain = it)
                    .save()
            }
        websites.forEach {
            createUserExcludedWebsite(id = null, userId = user.id!!, websiteId = it.id!!)
                .save()
        }
    }

    init {
        "findAllExcludedDomainsByUserId()" {
            val excludedDomains = customWebsiteRepository.findAllExcludedDomainsByUserId(user.id!!)

            excludedDomains shouldContainExactlyInAnyOrder DOMAINS
        }
    }
}
