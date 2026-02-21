package com.retoday.api.global.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.servers.Server
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Profile
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer

@Configuration
@Profile("!prod")
class SwaggerConfiguration {
    @Bean
    fun webSecurityCustomizer(): WebSecurityCustomizer =
        WebSecurityCustomizer { web ->
            web.ignoring().requestMatchers(
                "/v3/api-docs",
                "/v3/api-docs/**",
                "/swagger-ui/**",
                "/swagger-ui.html",
                "/swagger-resources/**",
                "/api-docs/**",
                "/favicon.ico",
                "/error",
                "/docs/**"
            )
        }

    @Bean
    fun openAPI(): OpenAPI =
        OpenAPI()
            .servers(
                listOf(
                    Server().url("/").description("Default Server URL")
                )
            )
}
