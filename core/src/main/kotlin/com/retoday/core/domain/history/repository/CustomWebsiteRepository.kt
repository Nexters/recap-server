package com.retoday.core.domain.history.repository

interface CustomWebsiteRepository {
    fun findAllExcludedDomainsByUserId(userId: Long): List<String>
}
