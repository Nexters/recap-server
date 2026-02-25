package com.retoday.core.domain.user.repository

import com.linecorp.kotlinjdsl.dsl.jpql.jpql
import com.linecorp.kotlinjdsl.render.RenderContext
import com.linecorp.kotlinjdsl.support.spring.data.jpa.extension.createQuery
import com.retoday.core.domain.user.dto.projection.ProfileWithEmailProjection
import com.retoday.core.domain.user.entity.Profile
import com.retoday.core.domain.user.entity.User
import jakarta.persistence.EntityManager

class CustomProfileRepositoryImpl(
    private val entityManager: EntityManager,
    private val renderContext: RenderContext
) : CustomProfileRepository {
    override fun findByUserIdWithEmail(userId: Long): ProfileWithEmailProjection? {
        val query =
            jpql {
                selectNew<Pair<Profile, String>>(
                    entity(Profile::class),
                    path(User::email)
                ).from(
                    entity(Profile::class),
                    join(User::class)
                        .on(path(User::id).equal(path(Profile::userId)))
                ).where(
                    path(User::id).equal(userId)
                )
            }
        val result =
            entityManager
                .createQuery(query, renderContext)
                .resultList
        val (profile, email) = result.firstOrNull() ?: return null

        return ProfileWithEmailProjection(
            profile = profile,
            email = email
        )
    }
}
