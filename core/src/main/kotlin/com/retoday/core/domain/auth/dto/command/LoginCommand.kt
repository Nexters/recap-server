package com.retoday.core.domain.auth.dto.command

import com.retoday.core.domain.user.entity.Provider

data class LoginCommand(
    val oAuthToken: String,
    val provider: Provider
)
