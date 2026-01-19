package com.recap.batch.global.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.recap.batch", "com.recap.core"])
class ScanConfiguration
