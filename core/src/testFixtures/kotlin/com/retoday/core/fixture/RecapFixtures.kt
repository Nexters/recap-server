package com.retoday.core.fixture

import com.retoday.core.domain.recap.dto.*
import com.retoday.core.domain.recap.entity.RecapEntity
import java.time.Instant
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

fun createUserActivities(): List<UserActivityDto> =
    listOf(
        UserActivityDto(
            title = "Spring Boot 멀티 모듈 설정", // p.title
            description = "멀티 모듈 구조 정리", // p.description
            domain = "velog.io", // w.domain
            categoryName = "개발", // c.name
            stayDuration = 1800 // h.stayDuration
        )
    )

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
                    startAt = "10:00",
                    endAt = "11:30",
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

fun createRecapEntity(
    id: Long? = 100L, // 테스트에서 검증용으로 사용할 ID
    userId: Long = 1L,
    recapDate: LocalDate = LocalDate.now(),
    title: String = "오늘의 보람찬 하루 요약",
    summary: String = "오늘은 주로 개발 업무와 기술 블로그 탐독을 하며 시간을 보냈습니다.",
    startAt: LocalDateTime = LocalDateTime.now().minusHours(9),
    closeAt: LocalDateTime = LocalDateTime.now(),
    model: String = "gemini-1.5-flash",
    createdAt: Instant = Instant.now()
): RecapEntity =
    RecapEntity(
        id = id,
        userId = userId,
        recapDate = recapDate,
        title = title,
        summary = summary,
        startAt = startAt,
        closeAt = closeAt,
        model = model,
        createdAt = createdAt
    )

fun createUserTimelineActivities(count: Int = 2): List<UserTimelineDto> {
    val now = Instant.now()

    return (1..count).map { i ->
        UserTimelineDto(
            title = "활동 제목 $i",
            description = "상세 설명 $i",
            categoryName = if (i % 2 == 0) "개발" else "커뮤니케이션",
            // i에 따라 시간을 뒤로 밀어서 생성 (활동 1: 2시간 전, 활동 2: 1시간 전)
            visitedAt = now.minus((count - i + 1).toLong(), ChronoUnit.HOURS),
            closedAt = now.minus((count - i).toLong(), ChronoUnit.HOURS).minus(10, ChronoUnit.MINUTES)
        )
    }
}
