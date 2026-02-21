package com.retoday.core.global.jwt

import com.retoday.core.domain.auth.exception.InvalidAuthenticationException
import com.retoday.core.domain.user.entity.User
import com.retoday.core.global.properties.JwtProperties
import io.jsonwebtoken.JwtException
import io.jsonwebtoken.Jwts
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.*

@Component
class JwtProvider(
    private val jwtProperties: JwtProperties
) {
    companion object {
        private const val TOKEN_ISSUER = "retoday"
    }

    fun createToken(
        expiration: Duration,
        user: User
    ): String =
        with(user) {
            createToken(
                expiration,
                mapOf(
                    ::id.name to id.toString(),
                    ::roles.name to roles.joinToString(",")
                )
            )
        }

    fun extractUserId(token: String): Long =
        try {
            extractPayload(token)
                .run { get(User::id.name) as String }
                .toLong()
        } catch (exception: JwtException) {
            throw InvalidAuthenticationException()
        }

    fun createToken(
        expiration: Duration,
        payload: Map<String, *>
    ): String {
        val now = Date()

        return Jwts
            .builder()
            .issuedAt(now)
            .expiration(Date(now.time + expiration.toMillis()))
            .issuer(TOKEN_ISSUER)
            .claims(payload)
            .signWith(jwtProperties.secretKey)
            .compact()
    }

    fun extractPayload(token: String): Map<String, *> =
        Jwts
            .parser()
            .verifyWith(jwtProperties.secretKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
