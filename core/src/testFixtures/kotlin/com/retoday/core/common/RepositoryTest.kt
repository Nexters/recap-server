package com.retoday.core.common

import com.linecorp.kotlinjdsl.support.spring.data.jpa.autoconfigure.KotlinJdslAutoConfiguration
import com.retoday.core.global.config.JpaConfiguration
import io.kotest.core.spec.style.StringSpec
import jakarta.persistence.EntityManager
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ContextConfiguration

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(
    classes = [
        JpaConfiguration::class,
        KotlinJdslAutoConfiguration::class
    ]
)
abstract class RepositoryTest : StringSpec() {
    @Autowired
    protected lateinit var entityManager: EntityManager

    protected fun <T> T.save(): T = also { entityManager.persist(it) }
}
