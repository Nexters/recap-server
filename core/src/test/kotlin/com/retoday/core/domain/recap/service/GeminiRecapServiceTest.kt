package com.retoday.core.domain.recap.service

import com.retoday.core.domain.recap.dto.UserActivityDto
import io.kotest.core.spec.style.FunSpec
import io.kotest.extensions.spring.SpringExtension
import io.kotest.matchers.shouldNotBe
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest(
    classes = [
        com.retoday.core.global.config.GeminiConfiguration::class,
        GeminiRecapService::class,
        com.retoday.core.domain.recap.component.RecapPromptManager::class,
        com.fasterxml.jackson.databind.ObjectMapper::class
    ]
)
@ActiveProfiles("test")
class GeminiRecapServiceTest(
    private val geminiRecapService: GeminiRecapService
) : FunSpec({

        extensions(SpringExtension)
        // 테스트 용 임시 데이터, 추후 수정 예정
        test("사용자의 활동 기록을 바탕으로 AI 리캡이 정상적으로 생성되어야 한다") {
            val nickname = "민주"
            val mockActivities =
                listOf(
                    UserActivityDto(
                        title = "Spring Boot 멀티 모듈 설정하기",
                        domain = "velog.io",
                        category = "개발",
                        duration = 1200,
                        description = "gradle을 이용한 멀티모듈 프로젝트 세팅에 관한 글입니다"
                    ),
                    UserActivityDto(
                        title = "[Spring Boot] 멀티 모듈 구조 적용기",
                        domain = "tistory.com",
                        category = "개발",
                        duration = 1000,
                        description = "최근 멀티 모듈 구조를 도입했다..."
                    ),
                    UserActivityDto(
                        title = "2024 맥북 에어 M3 리뷰",
                        domain = "youtube.com",
                        category = "엔터테인먼트",
                        duration = 300,
                        description = ""
                    ),
                    UserActivityDto(
                        title = "‘황제’ 스톨츠, 올림픽 신기록 제패 [2026 밀라노]",
                        domain = "naver.com",
                        category = "뉴스",
                        duration = 350,
                        description = ""
                    ),
                    UserActivityDto(
                        title = "김민재, 가치 증명...브레멘전 3-0 완승 견인",
                        domain = "naver.com",
                        category = "뉴스",
                        duration = 600,
                        description = ""
                    )
                )

            val response1 = geminiRecapService.generateRecap(nickname, mockActivities)
            val response3 = geminiRecapService.generateTopics(nickname, mockActivities)

            response1 shouldNotBe null

            println("AI가 생성한 실제 응답 결과:")
            println("---------------------------------")
            println(response1)
            println("---------------------------------")
            println(response3)
        }
    })
