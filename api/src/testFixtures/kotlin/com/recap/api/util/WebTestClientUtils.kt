package com.recap.api.util

import io.kotest.matchers.shouldBe
import org.springframework.http.HttpStatus
import org.springframework.test.web.reactive.server.WebTestClient.BodySpec
import org.springframework.test.web.reactive.server.WebTestClient.ResponseSpec
import org.springframework.test.web.reactive.server.expectBody

fun ResponseSpec.expectStatus(status: HttpStatus): ResponseSpec =
    expectStatus()
        .isEqualTo(status)

inline fun <reified T : Any> ResponseSpec.expectBody(body: T): BodySpec<T, *> =
    expectBody<T>()
        .consumeWith { it.responseBody shouldBe body }
