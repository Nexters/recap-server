package com.retoday.core.domain.user.entity

import com.retoday.core.fixture.createGetOAuthUserResponse
import com.retoday.core.fixture.createUser
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class UserTest : BehaviorSpec() {
    private val user = createUser()

    init {
        Given("가입한 사용자가") {
            And("소셜 프로필과 다른 사용자 정보를 가지고 있는 경우") {
                val changedEmail = "1117mg@re-today.com"
                val getOAuthUserResponse = createGetOAuthUserResponse(email = changedEmail)

                When("소셜 프로필 정보와 사용자 정보를 동기화하면") {
                    user.synchronizeOAuthUser(getOAuthUserResponse)

                    Then("이메일이 업데이트된다.") {
                        user.email shouldBe changedEmail
                    }
                }
            }
        }
    }
}
