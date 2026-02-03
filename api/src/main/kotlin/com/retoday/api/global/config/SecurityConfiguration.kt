package com.retoday.api.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.retoday.api.global.exception.SecurityExceptionHandler
import com.retoday.api.global.security.JwtAuthenticationFilter
import com.retoday.core.domain.user.entity.Role
import com.retoday.core.global.jwt.JwtProvider
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    private companion object {
        const val ADMIN_ENDPOINT_PREFIX = "/api/v1/admin"
        val PERMITTED_AUTH_ENDPOINTS =
            arrayOf(
                "/api/v1/auth/login",
                "/api/v1/auth/refresh"
            )
        val PERMITTED_ACTUATOR_ENDPOINTS = arrayOf("/actuator/health")
    }

    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        securityExceptionHandler: SecurityExceptionHandler,
        jwtAuthenticationFilter: JwtAuthenticationFilter
    ): SecurityFilterChain =
        with(http) {
            csrf { it.disable() }
            formLogin { it.disable() }
            logout { it.disable() }
            httpBasic { it.disable() }
            sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            exceptionHandling {
                it
                    .authenticationEntryPoint(securityExceptionHandler)
                    .accessDeniedHandler(securityExceptionHandler)
            }
            authorizeHttpRequests {
                it
                    .requestMatchers("$ADMIN_ENDPOINT_PREFIX/**")
                    .hasRole(Role.ADMIN.name)
                    .requestMatchers(
                        *PERMITTED_AUTH_ENDPOINTS,
                        *PERMITTED_ACTUATOR_ENDPOINTS
                    ).permitAll()
                    .anyRequest()
                    .authenticated()
            }
            addFilterAt(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter::class.java)
            build()
        }

    @Bean
    fun securityExceptionHandler(objectMapper: ObjectMapper): SecurityExceptionHandler =
        SecurityExceptionHandler(objectMapper = objectMapper)

    @Bean
    fun jwtAuthenticationFilter(jwtProvider: JwtProvider): JwtAuthenticationFilter =
        JwtAuthenticationFilter(jwtProvider = jwtProvider)
}
