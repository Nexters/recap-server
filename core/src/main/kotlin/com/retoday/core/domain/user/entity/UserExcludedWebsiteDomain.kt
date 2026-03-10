package com.retoday.core.domain.user.entity

import com.retoday.core.global.entity.BaseEntity
import org.springframework.data.relational.core.mapping.Table

@Table("user_excluded_website_domain")
data class UserExcludedWebsiteDomain(
    val userId: Long,
    val domain: String
) : BaseEntity()
