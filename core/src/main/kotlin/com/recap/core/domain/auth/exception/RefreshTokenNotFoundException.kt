package com.recap.core.domain.auth.exception

import com.recap.core.global.exception.ServerException

class RefreshTokenNotFoundException(
    override val message: String = "존재하지 않는 리프레시 토큰입니다."
) : ServerException(status = 404, message)
