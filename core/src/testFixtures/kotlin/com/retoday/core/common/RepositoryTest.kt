package com.retoday.core.common

import com.retoday.core.global.config.JdbcConfiguration
import io.kotest.core.spec.style.StringSpec
import io.kotest.core.test.TestCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.jdbc.DataJdbcTest
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers as EnableTestContainers

@DataJdbcTest
@EnableTestContainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [
        JdbcConfiguration::class
    ]
)
abstract class RepositoryTest : StringSpec() {
    companion object {
        @Container
        @ServiceConnection
        @JvmStatic
        private val mysql = MySQLContainer("mysql:8.0")
    }

    @Autowired
    protected lateinit var jdbcTemplate: JdbcTemplate

    override suspend fun beforeEach(testCase: TestCase) {
        withContext(Dispatchers.IO) {
            jdbcTemplate.batchUpdate(
                "DELETE FROM topic",
                "DELETE FROM timeline",
                "DELETE FROM section",
                "DELETE FROM recap",
                "DELETE FROM user_excluded_website_domain",
                "DELETE FROM history",
                "DELETE FROM page",
                "DELETE FROM website",
                "DELETE FROM website_category",
                "DELETE FROM profile",
                "DELETE FROM `user`"
            )
        }
    }
}
