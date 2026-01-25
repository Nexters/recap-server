package com.recap.core.fixture

import com.recap.core.domain.user.entity.User
import com.recap.core.global.jwt.JwtProvider
import com.recap.core.global.properties.JwtProperties
import io.mockk.every
import io.mockk.mockk
import java.time.Duration

const val TOKEN = "asddaadaddadsdasdasadsads"
val jwtProvider =
    mockk<JwtProvider>()
        .also {
            every { it.createToken(any(), any<User>()) } returns TOKEN
        }
val jwtProperties =
    mockk<JwtProperties>()
        .also {
            every { it.accessTokenExpiration } returns Duration.ofHours(24)
            every { it.refreshTokenExpiration } returns Duration.ofDays(7)
        }
