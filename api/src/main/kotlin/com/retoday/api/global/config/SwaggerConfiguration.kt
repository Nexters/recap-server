package com.retoday.api.global.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.security.SecurityRequirement
import io.swagger.v3.oas.models.security.SecurityScheme
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
    fun openAPI(): OpenAPI {
        val securityScheme =
            SecurityScheme()
                .type(SecurityScheme.Type.HTTP)
                .scheme("bearer")
                .bearerFormat("JWT")
                .`in`(SecurityScheme.In.HEADER)
                .name("Authorization")
        val securityRequirement = SecurityRequirement().addList("bearerAuth")

        return OpenAPI()
            .servers(
                listOf(
                    Server().url("/").description("Default Server URL")
                )
            ).components(
                Components()
                    .addSecuritySchemes("bearerAuth", securityScheme)
            ).security(listOf(securityRequirement))
    }
}
