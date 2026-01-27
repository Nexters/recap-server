package com.recap.core.global.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.time.Duration
import javax.crypto.SecretKey

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessTokenExpiration: Duration,
    val refreshTokenExpiration: Duration,
    val secretKey: SecretKey
)
