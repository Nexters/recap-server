package com.retoday.core.domain.user.entity

import com.retoday.core.fixture.createGetOAuthUserResponse
import com.retoday.core.fixture.createProfile
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe

class ProfileTest : BehaviorSpec() {
    init {
        Given("가입한 사용자가") {
            val profile = createProfile()

            And("소셜 프로필과 다른 사용자 정보를 가지고 있는 경우") {
                val changedFirstName = "Yelim"
                val changedLastName = "Lee"
                val changedImageUrl = "https://re-today.com/changed_profile.png"
                val getOAuthUserResponse =
                    createGetOAuthUserResponse(
                        firstName = changedFirstName,
                        lastName = changedLastName,
                        imageUrl = changedImageUrl
                    )

                When("소셜 프로필과 사용자 정보를 동기화하면") {
                    profile.synchronizeOAuthUser(getOAuthUserResponse)

                    Then("이름과 성, 프로필 이미지가 업데이트된다.") {
                        with(profile) {
                            firstName shouldBe changedFirstName
                            lastName shouldBe changedLastName
                            imageUrl shouldBe changedImageUrl
                        }
                    }
                }
            }
        }
    }
}
