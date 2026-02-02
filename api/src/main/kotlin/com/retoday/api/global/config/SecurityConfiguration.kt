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
                    .requestMatchers("/api/v1/admin/**")
                    .hasRole(Role.ADMIN.name)
                    .requestMatchers("/api/v1/auth/**", "/actuator/**")
                    .permitAll()
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
