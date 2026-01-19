package com.recap.api.global.config

import com.fasterxml.jackson.databind.ObjectMapper
import com.recap.api.global.exception.SecurityExceptionHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
class SecurityConfiguration {
    @Bean
    fun securityFilterChain(
        http: HttpSecurity,
        securityExceptionHandler: SecurityExceptionHandler
    ): SecurityFilterChain =
        with(http) {
            csrf { it.disable() }
            formLogin { it.disable() }
            logout { it.disable() }
            sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            exceptionHandling {
                it
                    .authenticationEntryPoint(securityExceptionHandler)
                    .accessDeniedHandler(securityExceptionHandler)
            }
            authorizeHttpRequests {
                it
                    .anyRequest()
                    .authenticated()
            }
            build()
        }

    @Bean
    fun securityExceptionHandler(objectMapper: ObjectMapper): SecurityExceptionHandler =
        SecurityExceptionHandler(objectMapper = objectMapper)
}
