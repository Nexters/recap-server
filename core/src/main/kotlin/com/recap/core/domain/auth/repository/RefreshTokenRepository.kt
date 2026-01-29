package com.recap.core.domain.auth.repository

import com.recap.core.domain.auth.entity.RefreshToken
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository

@Repository
interface RefreshTokenRepository : CrudRepository<RefreshToken, Long>
