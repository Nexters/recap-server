package com.retoday.api.snippet

import com.retoday.api.global.dto.ErrorResponse
import com.retoday.api.util.desc
import com.retoday.api.util.fieldsOf

val errorResponseFields =
    fieldsOf(
        ErrorResponse::code desc "에러 코드",
        ErrorResponse::message desc "에러 메세지"
    )
