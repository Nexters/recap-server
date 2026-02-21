package com.retoday.batch.global.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.retoday.batch", "com.retoday.core"])
class ScanConfiguration
