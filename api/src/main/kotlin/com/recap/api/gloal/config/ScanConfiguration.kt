package com.recap.api.gloal.config

import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.Configuration

@Configuration
@ComponentScan(basePackages = ["com.recap.api", "com.recap.core"])
class ScanConfiguration
