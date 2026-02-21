package com.retoday.core.global.util

import com.retoday.core.domain.history.exception.InvalidUrlException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe

class UrlUtilsTest :
    DescribeSpec({

        describe("extractDomain()은") {
            context("유효한 URL이 주어진 경우") {
                it("도메인을 추출한다") {
                    UrlUtils.extractDomain("https://github.com/user/repo") shouldBe "github.com"
                }

                it("www 접두사를 제거한다") {
                    UrlUtils.extractDomain("https://www.github.com/user/repo") shouldBe "github.com"
                }

                it("포트를 제외한 호스트를 반환한다") {
                    UrlUtils.extractDomain("http://api.example.com:8080/path") shouldBe "api.example.com"
                }

                it("서브도메인을 유지한다") {
                    UrlUtils.extractDomain("https://api.github.com/users") shouldBe "api.github.com"
                }
            }

            context("유효하지 않은 URL이 주어진 경우") {
                it("InvalidUrlException을 던진다") {
                    shouldThrow<InvalidUrlException> {
                        UrlUtils.extractDomain("invalid-url")
                    }
                }
            }
        }

        describe("normalizeUrl()은") {
            context("쿼리 파라미터가 있는 URL이 주어진 경우") {
                it("쿼리 파라미터를 제거한다") {
                    UrlUtils.normalizeUrl("https://github.com/search?q=test&page=1") shouldBe
                        "https://github.com/search"
                }
            }

            context("프래그먼트가 있는 URL이 주어진 경우") {
                it("프래그먼트를 제거한다") {
                    UrlUtils.normalizeUrl("https://github.com/user/repo#readme") shouldBe
                        "https://github.com/user/repo"
                }
            }

            context("쿼리 파라미터와 프래그먼트가 모두 있는 경우") {
                it("둘 다 제거한다") {
                    UrlUtils.normalizeUrl("https://github.com/user/repo?tab=readme#L10") shouldBe
                        "https://github.com/user/repo"
                }
            }

            context("경로만 있는 URL이 주어진 경우") {
                it("그대로 반환한다") {
                    UrlUtils.normalizeUrl("https://github.com/user/repo") shouldBe
                        "https://github.com/user/repo"
                }
            }
        }

        describe("extractPath()는") {
            it("URL에서 경로를 추출한다") {
                UrlUtils.extractPath("https://github.com/user/repo") shouldBe "/user/repo"
            }

            it("경로가 없으면 빈 문자열을 반환한다") {
                UrlUtils.extractPath("https://github.com") shouldBe ""
            }
        }

        describe("isValidUrl()은") {
            it("유효한 URL에 대해 true를 반환한다") {
                UrlUtils.isValidUrl("https://github.com/user/repo") shouldBe true
            }

            it("유효하지 않은 URL에 대해 false를 반환한다") {
                UrlUtils.isValidUrl("invalid-url") shouldBe false
            }
        }

        describe("extractQueryParams()는") {
            it("쿼리 파라미터를 Map으로 반환한다") {
                val params = UrlUtils.extractQueryParams("https://example.com/search?q=test&page=2")
                params shouldBe mapOf("q" to "test", "page" to "2")
            }

            it("URL 인코딩된 값을 디코딩한다") {
                val params = UrlUtils.extractQueryParams("https://example.com/search?q=%ED%85%8C%EC%8A%A4%ED%8A%B8")
                params shouldBe mapOf("q" to "테스트")
            }

            it("쿼리 파라미터가 없으면 빈 Map을 반환한다") {
                val params = UrlUtils.extractQueryParams("https://example.com/path")
                params shouldBe emptyMap()
            }
        }
    })
