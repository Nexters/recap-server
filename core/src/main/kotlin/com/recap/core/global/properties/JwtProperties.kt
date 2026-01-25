package com.recap.core.global.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import java.security.interfaces.RSAPrivateKey
import java.security.interfaces.RSAPublicKey
import java.time.Duration

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val accessTokenExpiration: Duration,
    val refreshTokenExpiration: Duration,
    val publicKey: RSAPublicKey,
    val privateKey: RSAPrivateKey
)
