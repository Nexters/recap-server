package com.retoday.core.global.extension

inline fun <T> T?.orElse(block: () -> T): T = this ?: block()
