package com.recap.api.global.exception

import com.recap.core.global.exception.ServerException

data class InvalidRequestException(
    override val message: String = "잘못된 요청입니다."
) : ServerException(status = 400, message)
