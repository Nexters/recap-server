package com.retoday.api.domain.user.controller

import com.retoday.api.domain.user.dto.response.GetMyProfileResponse
import com.retoday.api.global.annotation.AuthenticationId
import com.retoday.core.domain.user.service.UserService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/me/profiles")
    fun getMyProfile(
        @AuthenticationId
        userId: Long
    ): GetMyProfileResponse =
        userService
            .getMyProfile(userId)
            .let { GetMyProfileResponse.from(it) }
}
