package com.retoday.api.snippet

import com.retoday.api.domain.history.dto.request.HistoryRecordBatchRequest
import com.retoday.api.domain.history.dto.request.HistoryRecordRequest
import com.retoday.api.domain.history.dto.response.HistoryRecordBatchResponse
import com.retoday.api.domain.history.dto.response.HistoryRecordResponse
import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf
import com.retoday.api.extension.listFieldsOf
import com.retoday.api.extension.objectFieldsOf

val historyRecordRequestFields =
    fieldsOf(
        HistoryRecordRequest::tabId desc "탭 ID",
        HistoryRecordRequest::url desc "페이지 URL (최대 2048자)",
        HistoryRecordRequest::visitedAt desc "방문 시작 시각 (ISO 8601)",
        HistoryRecordRequest::closedAt desc "탭 종료/이동 시각 (ISO 8601)",
        HistoryRecordRequest::title desc "페이지 제목 (최대 500자)",
        HistoryRecordRequest::isFinal desc "탭 종료 여부 (true: 종료, false: 이동)",
        HistoryRecordRequest::scrollDepth desc "최대 스크롤 깊이 (0-100)"
    ) +
        objectFieldsOf(
            HistoryRecordRequest::metadata desc "페이지 메타데이터",
            "description" desc "페이지 설명 (최대 5000자)",
            "thumbnailUrl" desc "썸네일 URL (최대 500자)"
        )

val historyRecordResponseFields =
    fieldsOf(
        HistoryRecordResponse::historyId desc "생성된 히스토리 ID",
        HistoryRecordResponse::pageId desc "페이지 ID",
        HistoryRecordResponse::websiteId desc "웹사이트(도메인) ID",
        HistoryRecordResponse::stayDuration desc "체류 시간 (초)",
        HistoryRecordResponse::recordedAt desc "기록 생성 시각"
    )

val historyRecordBatchRequestFields =
    listFieldsOf(
        HistoryRecordBatchRequest::records desc "기록 요청 목록 (최대 100개)",
        "tabId" desc "탭 ID",
        "url" desc "페이지 URL",
        "visitedAt" desc "방문 시작 시각",
        "closedAt" desc "탭 종료/이동 시각",
        "title" desc "페이지 제목",
        "metadata" desc "페이지 메타데이터",
        "metadata.description" desc "페이지 설명",
        "metadata.thumbnailUrl" desc "썸네일 URL",
        "isFinal" desc "탭 종료 여부",
        "scrollDepth" desc "최대 스크롤 깊이"
    )

val historyRecordBatchResponseFields =
    fieldsOf(
        HistoryRecordBatchResponse::successCount desc "성공 개수",
        HistoryRecordBatchResponse::failedCount desc "실패 개수"
    ) +
        listFieldsOf(
            HistoryRecordBatchResponse::results desc "개별 결과 목록",
            "tabId" desc "탭 ID",
            "success" desc "성공 여부",
            "historyId" desc "생성된 히스토리 ID (성공 시)",
            "errorCode" desc "에러 코드 (실패 시)",
            "errorMessage" desc "에러 메시지 (실패 시)"
        )
