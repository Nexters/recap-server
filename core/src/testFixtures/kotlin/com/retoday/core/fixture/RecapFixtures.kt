package com.retoday.core.fixture

import com.retoday.core.domain.recap.dto.*

fun createUserActivities(): List<UserActivityDto> =
    listOf(
        UserActivityDto(
            title = "Spring Boot 멀티 모듈 설정",
            domain = "velog.io",
            category = "개발",
            duration = 1800,
            description = "멀티 모듈 구조 정리"
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
