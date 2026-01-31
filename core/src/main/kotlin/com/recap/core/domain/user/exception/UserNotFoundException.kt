package com.recap.core.domain.user.exception

import com.recap.core.global.exception.ServerException

class UserNotFoundException(
    override val message: String = "존재하지 않는 사용자입니다."
) : ServerException(status = 404, message)
