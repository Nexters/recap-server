package com.recap.api.snippet

import com.recap.api.global.dto.ErrorResponse
import com.recap.api.util.desc
import com.recap.api.util.fieldsOf

val errorResponseFields =
    fieldsOf(
        ErrorResponse::code desc "에러 코드",
        ErrorResponse::message desc "에러 메세지"
    )
