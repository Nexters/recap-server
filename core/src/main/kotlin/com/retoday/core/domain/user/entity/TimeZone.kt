package com.retoday.core.domain.user.entity

import java.time.ZoneId

enum class TimeZone(
    val id: ZoneId
) {
    SEOUL(ZoneId.of("Asia/Seoul"))
}
