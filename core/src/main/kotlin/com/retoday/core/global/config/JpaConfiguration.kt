package com.retoday.core.global.config

import org.springframework.boot.autoconfigure.domain.EntityScan
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaAuditing
import org.springframework.data.jpa.repository.config.EnableJpaRepositories

@Configuration
@EnableJpaAuditing
@EnableJpaRepositories(basePackages = ["com.retoday.core.domain"])
@EntityScan(basePackages = ["com.retoday.core.domain"])
class JpaConfiguration
