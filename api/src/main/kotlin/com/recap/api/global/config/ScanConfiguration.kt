package com.recap.api.global.config

import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ConfigurationPropertiesScan(basePackages = ["com.recap.core"])
@ComponentScan(basePackages = ["com.recap.api", "com.recap.core"])
class ScanConfiguration
