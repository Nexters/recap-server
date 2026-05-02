package com.retoday.api.snippet

import com.retoday.api.domain.user.dto.request.AddMyExcludedDomainRequest
import com.retoday.api.domain.user.dto.request.DeleteMyExcludedDomainRequest
import com.retoday.api.domain.user.dto.request.UpdateMyLanguageRequest
import com.retoday.api.domain.user.dto.response.GetMyProfileResponse
import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf

val addMyExcludedDomainRequestFields =
    fieldsOf(
        AddMyExcludedDomainRequest::domain desc "예외 처리할 도메인"
    )

val deleteMyExcludedDomainRequestFields =
    fieldsOf(
        DeleteMyExcludedDomainRequest::domain desc "예외 처리에서 제거할 도메인"
    )

val updateMyLanguageRequestFields =
    fieldsOf(
        UpdateMyLanguageRequest::language desc "언어(KO, EN, JA)"
    )

val getMyProfileResponseFields =
    fieldsOf(
        GetMyProfileResponse::id desc "식별자",
        GetMyProfileResponse::email desc "이메일",
        GetMyProfileResponse::firstName desc "이름",
        GetMyProfileResponse::lastName desc "성",
        GetMyProfileResponse::imageUrl desc "프로필 이미지 URL",
        GetMyProfileResponse::timeZone desc "타임존",
        GetMyProfileResponse::recapPeriod desc "리캡 생성 주기",
        GetMyProfileResponse::language desc "언어(KO, EN, JA)",
        GetMyProfileResponse::excludedDomains desc "예외 도메인 리스트"
    )
