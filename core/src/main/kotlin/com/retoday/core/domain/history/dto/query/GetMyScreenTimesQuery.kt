package com.retoday.core.domain.history.dto.query

import java.time.DayOfWeek
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import kotlin.time.Duration
import kotlin.time.Duration.Companion.days
import kotlin.time.Duration.Companion.hours

data class GetMyScreenTimesQuery(
    val date: LocalDate,
    val period: Period
) {
    enum class Period(
        val screenTimeDuration: Duration,
        val screenTimeUnit: Duration
    ) {
        DAILY(1.days, 2.hours),
        WEEKLY(7.days, 1.days);

        val screenTimeCount = (screenTimeDuration.inWholeHours / screenTimeUnit.inWholeHours).toInt()

        fun getStartedAt(date: LocalDate): LocalDate =
            when (this) {
                DAILY -> date
                WEEKLY -> date.with(TemporalAdjusters.previousOrSame(DayOfWeek.SUNDAY))
            }
    }
}
