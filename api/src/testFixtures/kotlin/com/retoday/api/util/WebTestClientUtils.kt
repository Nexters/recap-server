package com.retoday.api.util

import com.retoday.api.global.dto.ErrorResponse
import io.kotest.matchers.shouldBe
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.reactive.server.expectBody

fun ResponseSpec.expectStatus(status: Int): ResponseSpec =
    expectStatus()
        .isEqualTo(status)

inline fun <reified T : Any> ResponseSpec.expectBody(body: T): BodySpec<T, *> =
    expectBody<T>()
        .consumeWith { it.responseBody shouldBe body }

fun ResponseSpec.expectError(): BodySpec<ErrorResponse, *> = expectBody<ErrorResponse>()
