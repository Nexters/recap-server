package com.retoday.api.common

import io.kotest.core.spec.style.DescribeSpec
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.restdocs.AutoConfigureRestDocs
import org.springframework.restdocs.RestDocumentationContextProvider
import org.springframework.restdocs.webtestclient.WebTestClientRestDocumentation
import org.springframework.test.web.servlet.client.MockMvcWebTestClient
import org.springframework.web.context.WebApplicationContext

@AutoConfigureRestDocs
abstract class ControllerTest(
    private val version: Int = 1
) : DescribeSpec() {
    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    @Autowired
    private lateinit var restDocumentationContextProvider: RestDocumentationContextProvider

    protected val webClient by lazy {
        MockMvcWebTestClient
            .bindToApplicationContext(webApplicationContext)
            .configureClient()
            .baseUrl("/api/v$version")
            .filter(WebTestClientRestDocumentation.documentationConfiguration(restDocumentationContextProvider))
            .build()
    }
}
