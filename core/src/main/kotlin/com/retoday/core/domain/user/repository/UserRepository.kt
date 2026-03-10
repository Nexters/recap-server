package com.retoday.core.domain.user.repository

import com.retoday.core.domain.user.entity.Provider
import com.retoday.core.domain.user.entity.User
import org.springframework.data.repository.ListCrudRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : ListCrudRepository<User, Long> {
    fun findBySocialIdAndProvider(
        socialId: String,
        provider: Provider
    ): User?
}
