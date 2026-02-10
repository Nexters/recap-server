package com.retoday.core.domain.website.repository

import com.retoday.core.domain.history.entity.Website
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface WebsiteRepository :
    JpaRepository<Website, Long>,
    CustomWebsiteRepository
