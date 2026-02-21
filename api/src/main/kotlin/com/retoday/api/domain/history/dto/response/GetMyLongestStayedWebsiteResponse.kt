package com.retoday.api.domain.history.dto.response

import com.retoday.core.domain.history.dto.result.GetMyLongestStayedWebsiteResult
import java.time.LocalDate

data class GetMyLongestStayedWebsiteResponse(
    val date: LocalDate,
    val domain: String?,
    val faviconUrl: String?,
    val stayDuration: Long
) {
    companion object {
        fun from(result: GetMyLongestStayedWebsiteResult): GetMyLongestStayedWebsiteResponse =
            with(result) {
                GetMyLongestStayedWebsiteResponse(
                    date = date,
                    domain = domain,
                    faviconUrl = faviconUrl,
                    stayDuration = stayDuration
                )
            }
    }
}
