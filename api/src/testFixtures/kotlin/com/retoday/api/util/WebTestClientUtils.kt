package com.retoday.api.util

import com.retoday.api.fixture.createRetodayAuthentication
import com.retoday.api.global.dto.ErrorResponse
import io.kotest.matchers.shouldBe
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
): RequestHeadersSpec<*> = also { SecurityContextHolder.getContext().authentication = authentication }
