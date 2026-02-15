package com.retoday.api.snippet

import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.request.PageMetadata
import com.retoday.api.domain.history.dto.response.GetMyCategoryAnalysesResponse
import com.retoday.api.domain.history.dto.response.GetMyScreenTimesResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf
import com.retoday.api.extension.listFieldsOf
import com.retoday.api.extension.objectFieldsOf
import com.retoday.core.domain.history.dto.result.GetMyCategoryAnalysesResult

val historyRecordRequestFields =
    fieldsOf(
        HistoryRecordRequest::tabId desc "탭 ID",
        HistoryRecordRequest::url desc "페이지 URL (최대 2048자)",
        HistoryRecordRequest::visitedAt desc "방문 시작 시각 (ISO 8601)",
        HistoryRecordRequest::closedAt desc "탭 종료/이동 시각 (ISO 8601)",
        HistoryRecordRequest::title desc "페이지 제목 (최대 500자)",
        HistoryRecordRequest::isClosed desc "탭 종료 여부 (true: 종료, false: 이동)",
        HistoryRecordRequest::scrollDepth desc "최대 스크롤 깊이 (0-100)",
        *objectFieldsOf(
            objectField = HistoryRecordRequest::metadata desc "페이지 메타데이터",
            PageMetadata::description desc "페이지 설명 (최대 5000자)",
            PageMetadata::faviconUrl desc "파비콘 URL (최대 500자)"
        )
    )

val historyRecordResponseFields =
    fieldsOf(
        HistoryRecordResponse::historyId desc "생성된 히스토리 ID",
        HistoryRecordResponse::pageId desc "페이지 ID",
        HistoryRecordResponse::websiteId desc "웹사이트(도메인) ID",
        HistoryRecordResponse::stayDuration desc "체류 시간 (초)",
        HistoryRecordResponse::recordedAt desc "기록 생성 시각"
    )

val getMyScreenTimesQueryFields =
    fieldsOf(
        "date" desc "조회 기준 일자(yyyy-MM-dd)",
        "period" desc "조회 기간 타입(DAILY, WEEKLY)"
    )

val getMyScreenTimesResponseFields =
    fieldsOf(
        GetMyScreenTimesResponse::period desc "조회 기간 타입",
        GetMyScreenTimesResponse::startedAt desc "조회 기간 시작일",
        GetMyScreenTimesResponse::endedAt desc "조회 기간 종료일",
        GetMyScreenTimesResponse::totalStayDuration desc "총 체류 시간(초)",
        *listFieldsOf(
            listField = GetMyScreenTimesResponse::screenTimes desc "구간별 체류 시간 목록",
            GetMyScreenTimesResponse.ScreenTimeResponse::startedAt desc "구간 시작 일시",
            GetMyScreenTimesResponse.ScreenTimeResponse::endedAt desc "구간 종료 일시",
            GetMyScreenTimesResponse.ScreenTimeResponse::stayDuration desc "구간 체류 시간(초)"
        )
    )

val getMyCategoryAnalysisQueryFields =
    fieldsOf(
        "date" desc "조회 기준 일자(yyyy-MM-dd)"
    )

val getMyCategoryAnalysesResponseFields =
    fieldsOf(
        GetMyCategoryAnalysesResponse::date desc "조회 기준 일자",
        *listFieldsOf(
            listField = GetMyCategoryAnalysesResponse::categoryAnalyses desc "카테고리별 분석 목록",
            GetMyCategoryAnalysesResult.CategoryAnalysis::categoryName desc "카테고리 이름",
            GetMyCategoryAnalysesResult.CategoryAnalysis::stayDuration desc "카테고리 총 체류 시간(초)",
            *listFieldsOf(
                listField = GetMyCategoryAnalysesResult.CategoryAnalysis::websiteAnalyses desc "카테고리 내 도메인 분석 목록",
                GetMyCategoryAnalysesResult.WebsiteAnalysis::domain desc "도메인",
                GetMyCategoryAnalysesResult.WebsiteAnalysis::faviconUrl desc "파비콘 URL",
                GetMyCategoryAnalysesResult.WebsiteAnalysis::stayDuration desc "도메인 체류 시간(초)"
            )
        )
    )
