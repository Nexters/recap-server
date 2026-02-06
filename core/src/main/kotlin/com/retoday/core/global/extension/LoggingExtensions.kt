package com.retoday.core.global.extension

import io.github.oshai.kotlinlogging.KLogger
import io.github.oshai.kotlinlogging.KotlinLogging

inline fun <reified T> T.getLogger(): KLogger = KotlinLogging.logger { T::class }
