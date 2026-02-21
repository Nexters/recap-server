package com.retoday.core.global.config

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.jdbc.datasource.LazyConnectionDataSourceProxy
import javax.sql.DataSource

@Configuration
class DataSourceConfiguration {
    @Bean
    @ConfigurationProperties(prefix = "datasource.main")
    fun mainHikariConfig(): HikariConfig = HikariConfig()

    @Bean
    fun mainDataSource(config: HikariConfig): DataSource = LazyConnectionDataSourceProxy(HikariDataSource(config))
}
