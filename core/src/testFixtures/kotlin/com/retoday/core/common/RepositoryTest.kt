package com.retoday.core.common

import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import com.retoday.core.global.config.JpaConfiguration
import com.retoday.core.global.entity.BaseEntity
import io.kotest.core.spec.style.StringSpec
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.testcontainers.service.connection.ServiceConnection
import org.springframework.test.context.ContextConfiguration
import org.testcontainers.containers.MySQLContainer
import org.testcontainers.junit.jupiter.Container
import org.testcontainers.junit.jupiter.Testcontainers as EnableTestContainers

@DataJpaTest
@EnableTestContainers
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [
        JpaConfiguration::class,
        KotlinJdslAutoConfiguration::class
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
    protected lateinit var entityManager: EntityManager

    protected fun <T : BaseEntity> T.save(): T = also { entityManager.persist(it) }
}
