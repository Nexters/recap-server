package com.retoday.core.fixture

import com.retoday.core.domain.website.entity.Website

const val DOMAIN = "retoday.com"

fun createWebsite(
    id: Long? = ID,
    domain: String = DOMAIN
): Website =
    Website(
        id = id,
        domain = domain
    )
