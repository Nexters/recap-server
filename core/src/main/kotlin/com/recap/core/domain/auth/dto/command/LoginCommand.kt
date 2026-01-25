package com.recap.core.domain.auth.dto.command

import com.recap.core.domain.user.entity.Provider

data class LoginCommand(
    val oAuthToken: String,
    val provider: Provider
)
