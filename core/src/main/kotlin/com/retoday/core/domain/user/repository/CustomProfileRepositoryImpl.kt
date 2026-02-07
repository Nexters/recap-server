package com.retoday.core.domain.user.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.retoday.core.domain.user.dto.projection.ProfileWithEmailAndExcludedDomains
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import com.retoday.core.domain.user.entity.UserExcludedWebsite
import com.retoday.core.domain.website.entity.Website
import jakarta.persistence.EntityManager

class CustomProfileRepositoryImpl(
    private val entityManager: EntityManager,
    private val renderContext: RenderContext
) : CustomProfileRepository {
    override fun findByUserIdWithEmailAndExcludedDomains(userId: Long): ProfileWithEmailAndExcludedDomains? {
        val query =
            jpql {
                selectNew<Triple<Profile, String, String>>(
                    entity(Profile::class),
                    path(User::email),
                    path(Website::domain)
                ).from(
                    entity(Profile::class),
                    join(User::class)
                        .on(path(User::id).equal(path(Profile::userId))),
                    leftJoin(UserExcludedWebsite::class)
                        .on(path(UserExcludedWebsite::userId).equal(path(User::id))),
                    leftJoin(Website::class)
                        .on(path(Website::id).equal(path(UserExcludedWebsite::websiteId)))
                ).where(
                    path(User::id).equal(userId)
                )
            }
        val results =
            entityManager
                .createQuery(query, renderContext)
                .resultList
                .ifEmpty { return null }

        val (profile, email) = results.first()
        val excludedDomains = results.mapNotNull { it.third }

        return ProfileWithEmailAndExcludedDomains(
            profile = profile,
            email = email,
            excludedDomains = excludedDomains
        )
    }
}
