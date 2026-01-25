package com.recap.core.domain.user.repository

import com.recap.core.domain.user.entity.Provider
import com.recap.core.domain.user.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {
    fun findBySocialIdAndProvider(
        socialId: String,
        provider: Provider
    ): User?
}
