package com.retoday.core.domain.website.repository

interface CustomWebsiteRepository {
    fun findAllExcludedDomainsByUserId(userId: Long): List<String>
}
