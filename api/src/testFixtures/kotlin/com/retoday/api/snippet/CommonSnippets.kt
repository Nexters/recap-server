package com.retoday.api.snippet

import com.retoday.api.extension.desc
import com.retoday.api.extension.fieldsOf
import com.retoday.api.global.dto.ErrorResponse

val errorResponseFields =
    fieldsOf(
        ErrorResponse::code desc "에러 코드",
        ErrorResponse::message desc "에러 메세지"
    )
