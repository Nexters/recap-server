package com.retoday.core.fixture

import com.retoday.core.domain.recap.dto.projection.UserActivityProjection
import com.retoday.core.domain.recap.dto.projection.UserTimelineProjection
import com.retoday.core.domain.recap.dto.request.UserActivityRequest
import com.retoday.core.domain.recap.dto.request.UserTimelineRequest
import com.retoday.core.domain.recap.dto.response.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.response.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.response.GeminiTopicResponse
import com.retoday.core.domain.recap.entity.Recap
import java.time.Instant
import java.time.LocalDate
import java.time.temporal.ChronoUnit

fun createUserActivities(): List<UserActivityProjection> =
    listOf(
        UserActivityProjection(
            title = "Spring Boot 멀티 모듈 설정", // p.title
            description = "멀티 모듈 구조 정리", // p.description
            domain = "velog.io", // w.domain
            categoryName = "개발", // c.name
            stayDuration = 1800L // h.stayDuration
        )
    )

fun createUserActivityRequests(activities: List<UserActivityProjection>): List<UserActivityRequest> =
    activities.map {
        UserActivityRequest(
            title = it.title,
            description = it.description,
            domain = it.domain,
            categoryName = it.categoryName,
            stayDuration = it.stayDuration.toInt()
        )
    }

fun createGeminiRecapResponse(): GeminiRecapResponse =
    GeminiRecapResponse(
        title = "개발에 집중한 하루였습니다",
        dailySummary = "멀티 모듈 구조를 깊게 이해했어요.",
        sections =
            listOf(
                GeminiRecapResponse.RecapSection(
                    title = "구조를 정리한 시간",
                    content = "Spring Boot 멀티 모듈 구조를 정리하며 프로젝트 구조 이해도를 높였습니다."
                ),
                GeminiRecapResponse.RecapSection(
                    title = "개념을 확장한 시간",
                    content = "관련 문서를 탐색하며 실제 적용 방법까지 고민하는 시간을 가졌습니다."
                )
            )
    )

fun createGeminiTimelineResponse(): GeminiTimelineResponse =
    GeminiTimelineResponse(
        timelines =
            listOf(
                GeminiTimelineResponse.TimelineItem(
                    startedAt = "10:00",
                    endedAt = "11:30",
                    title = "Spring Boot 구조 학습",
                    durationMinutes = 90
                )
            )
    )

fun createGeminiTopicResponse(): GeminiTopicResponse =
    GeminiTopicResponse(
        topics =
            listOf(
                GeminiTopicResponse.TopicItem(
                    keyword = "개발",
                    title = "개발에 집중했습니다",
                    content = "Spring Boot 관련 자료를 집중적으로 탐색했습니다."
                )
            )
    )

fun createRecap(
    id: Long? = 100L, // 테스트에서 검증용으로 사용할 ID
    userId: Long = 1L,
    recapDate: LocalDate = LocalDate.now(),
    title: String = "오늘의 보람찬 하루 요약",
    summary: String = "오늘은 주로 개발 업무와 기술 블로그 탐독을 하며 시간을 보냈습니다.",
    startedAt: Instant = Instant.now().minus(9, ChronoUnit.HOURS),
    closedAt: Instant = Instant.now(),
    model: String = "gemini-2.5-flash"
): Recap =
    Recap(
        id = id,
        userId = userId,
        recapDate = recapDate,
        title = title,
        summary = summary,
        startedAt = startedAt,
        closedAt = closedAt,
        model = model
    )

fun createUserTimelineActivities(): List<UserTimelineProjection> {
    // 기준 시간을 UTC Instant로 설정
    val baseTime = Instant.parse("2024-05-20T10:00:00Z")

    return listOf(
        UserTimelineProjection(
            title = "활동 제목 1",
            description = "상세 설명 1",
            categoryName = "개발",
            visitedAt = baseTime, // 변환 없이 그대로 사용
            closedAt = baseTime.plus(1, ChronoUnit.HOURS) // 바로 계산해서 사용
        ),
        UserTimelineProjection(
            title = "활동 제목 2",
            description = "상세 설명 2",
            categoryName = "디자인",
            visitedAt = baseTime.plus(2, ChronoUnit.HOURS),
            closedAt = baseTime.plus(3, ChronoUnit.HOURS)
        )
    )
}

fun createUserTimelineRequests(timelines: List<UserTimelineProjection>): List<UserTimelineRequest> =
    timelines.map {
        UserTimelineRequest(
            title = it.title,
            description = it.description,
            categoryName = it.categoryName,
            visitedAt = it.visitedAt,
            closedAt = it.closedAt
        )
    }
