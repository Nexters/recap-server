package com.retoday.core.global.config

import org.springframework.context.annotation.Configuration
import org.springframework.data.jdbc.repository.config.EnableJdbcAuditing
import org.springframework.data.jdbc.repository.config.EnableJdbcRepositories

@Configuration
@EnableJdbcAuditing
@EnableJdbcRepositories(basePackages = ["com.retoday.core.domain"])
class JdbcConfiguration
