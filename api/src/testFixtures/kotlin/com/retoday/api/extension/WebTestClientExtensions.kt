package com.retoday.api.extension

import com.retoday.api.fixture.createRetodayAuthentication
import com.retoday.api.global.dto.ErrorResponse
import com.retoday.core.fixture.TOKEN
import io.kotest.matchers.shouldBe
import org.springframework.http.HttpHeaders
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.test.web.reactive.server.WebTestClient.*
import org.springframework.test.web.reactive.server.expectBody

fun ResponseSpec.expectStatus(status: Int): ResponseSpec =
    expectStatus()
        .isEqualTo(status)

inline fun <reified T : Any> ResponseSpec.expectBody(body: T): BodySpec<T, *> =
    expectBody<T>()
        .consumeWith { it.responseBody shouldBe body }

fun ResponseSpec.expectError(): BodySpec<ErrorResponse, *> = expectBody<ErrorResponse>()

fun RequestHeadersSpec<*>.withAuthentication(
    authentication: Authentication = createRetodayAuthentication()
): RequestHeadersSpec<*> =
    header(HttpHeaders.AUTHORIZATION, "Bearer $TOKEN")
        .also { SecurityContextHolder.getContext().authentication = authentication }
