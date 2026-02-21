package com.retoday.core.domain.recap.service

import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.*
import com.retoday.core.domain.recap.entity.SectionEntity
import com.retoday.core.domain.recap.entity.TimelineEntity
import com.retoday.core.domain.recap.entity.TopicEntity
import com.retoday.core.domain.recap.repository.*
import com.retoday.core.fixture.*
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.*
import java.time.LocalDate

class RecapServiceTest :
    BehaviorSpec({

        // 1. 모든 의존성 Mocking
        val recapAIClient = mockk<RecapAIClient>()
        val recapRepository = mockk<RecapRepository>()
        val sectionRepository = mockk<SectionRepository>()
        val topicRepository = mockk<TopicRepository>()
        val timelineRepository = mockk<TimelineRepository>()
        val historyRepository = mockk<HistoryRepository>()

        val recapService =
            RecapService(
                recapAIClient,
                recapRepository,
                sectionRepository,
                topicRepository,
                timelineRepository,
                historyRepository
            )

        // 공통 데이터 설정
        val userId = 1L
        val nickname = "민주"
        val date = LocalDate.now()
        val activities = createUserActivities() // List<UserActivityDto>
        val timelineActivities = createUserTimelineActivities() // List<UserTimelineDto> (새로 추가 필요)

        Given("사용자가 특정 날짜에 활동 기록을 가지고 있을 때") {

            // DB 조회 Mocking
            every { historyRepository.findUserActivitiesForRecap(userId, date) } returns activities
            every { historyRepository.findUserTimelinesForRecap(userId, date) } returns timelineActivities
            every { historyRepository.findFirstByUserIdAndVisitedDateOrderByVisitedAtAsc(userId, date) } returns null
            every { historyRepository.findFirstByUserIdAndVisitedDateOrderByClosedAtDesc(userId, date) } returns null

            // AI 응답 Mocking
            val recapResponse = createGeminiRecapResponse()
            val topicResponse = createGeminiTopicResponse()
            val timelineResponse = createGeminiTimelineResponse()

            every { recapAIClient.modelName } returns "gemini-pro"
            every {
                recapAIClient.generate(RecapType.TODAY_RECAP, nickname, activities, GeminiRecapResponse::class.java)
            } returns recapResponse
            every {
                recapAIClient.generate(RecapType.TOPIC, nickname, activities, GeminiTopicResponse::class.java)
            } returns topicResponse
            every {
                recapAIClient.generateTimeline(
                    RecapType.TIMELINE,
                    nickname,
                    timelineActivities,
                    GeminiTimelineResponse::class.java
                )
            } returns timelineResponse

            // Entity 저장 Mocking
            val savedRecap = createRecapEntity(id = 100L)
            every { recapRepository.save(any()) } returns savedRecap
            every { sectionRepository.saveAll(any<List<SectionEntity>>()) } returns emptyList()
            every { topicRepository.saveAll(any<List<TopicEntity>>()) } returns emptyList()
            every { timelineRepository.saveAll(any<List<TimelineEntity>>()) } returns emptyList()

            When("createDailyRecap을 호출하여 리캡 생성을 수행하면") {
                recapService.createDailyRecap(userId, nickname, date)

                Then("부모 Recap이 저장되고, 파생되는 모든 엔티티들이 recapId(100L)를 가지고 저장된다") {
                    // 1. 부모 리캡 저장 확인
                    verify(exactly = 1) { recapRepository.save(any()) }

                    // 2. 섹션 저장 확인
                    verify(exactly = 1) {
                        sectionRepository.saveAll(
                            match<List<SectionEntity>> { sections ->
                                // 타입을 명시적으로 지정
                                sections.all { it.recapId == 100L }
                            }
                        )
                    }

                    // 3. 토픽 저장 확인
                    verify(exactly = 1) {
                        topicRepository.saveAll(
                            match<List<TopicEntity>> { topics ->
                                // 타입을 명시적으로 지정
                                topics.all { it.recapId == 100L }
                            }
                        )
                    }

                    // 4. 타임라인 저장 확인
                    verify(exactly = 1) {
                        timelineRepository.saveAll(
                            match<List<TimelineEntity>> { timelines ->
                                // 타입을 명시적으로 지정
                                timelines.all { it.recapId == 100L }
                            }
                        )
                    }
                }
            }
        }
    })
