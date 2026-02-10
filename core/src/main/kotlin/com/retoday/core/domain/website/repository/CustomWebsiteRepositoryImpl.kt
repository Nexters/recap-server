package com.retoday.core.domain.website.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.retoday.core.domain.user.entity.UserExcludedWebsite
import com.retoday.core.domain.history.entity.Website
import jakarta.persistence.EntityManager

class CustomWebsiteRepositoryImpl(
    private val entityManager: EntityManager,
    private val renderContext: RenderContext
) : CustomWebsiteRepository {
    override fun findAllExcludedDomainsByUserId(userId: Long): List<String> {
        val query =
            jpql {
                selectNew<String>(
                    path(Website::domain)
                ).from(
                    entity(Website::class),
                    join(UserExcludedWebsite::class)
                        .on(path(Website::id).equal(path(UserExcludedWebsite::websiteId)))
                ).where(
                    path(UserExcludedWebsite::userId).equal(userId)
                )
            }
        val result =
            entityManager
                .createQuery(query, renderContext)
                .resultList

        return result
    }
}
