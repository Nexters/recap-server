package com.retoday.api.snippet

import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf
import com.retoday.api.extension.listFieldsOf
import com.retoday.core.domain.recap.dto.response.RecapDetailResponse

val generateRecapQueryFields =
    fieldsOf(
        "date" desc "생성할 리캡 날짜(yyyy-MM-dd), 미입력 시 전일"
    )

val recapDetailResponseFields =
    fieldsOf(
        RecapDetailResponse::id desc "리캡 식별자",
        RecapDetailResponse::userId desc "사용자 식별자",
        RecapDetailResponse::recapDate desc "리캡 대상 일자",
        RecapDetailResponse::title desc "리캡 제목",
        RecapDetailResponse::summary desc "리캡 요약",
        RecapDetailResponse::startedAt desc "리캡 시작 시각",
        RecapDetailResponse::closedAt desc "리캡 종료 시각",
        *listFieldsOf(
            listField = RecapDetailResponse::sections desc "리캡 섹션 목록",
            RecapDetailResponse.SectionResponse::title desc "섹션 제목",
            RecapDetailResponse.SectionResponse::content desc "섹션 내용"
        ),
        *listFieldsOf(
            listField = RecapDetailResponse::timelines desc "리캡 타임라인 목록",
            RecapDetailResponse.TimelineResponse::startedAt desc "시작 시간(HH:mm)",
            RecapDetailResponse.TimelineResponse::endedAt desc "종료 시간(HH:mm)",
            RecapDetailResponse.TimelineResponse::title desc "타임라인 제목",
            RecapDetailResponse.TimelineResponse::durationMinutes desc "지속 시간(분)"
        ),
        *listFieldsOf(
            listField = RecapDetailResponse::topics desc "리캡 주제 목록",
            RecapDetailResponse.TopicResponse::keyword desc "주제 키워드",
            RecapDetailResponse.TopicResponse::title desc "주제 제목",
            RecapDetailResponse.TopicResponse::content desc "주제 내용"
        )
    )
