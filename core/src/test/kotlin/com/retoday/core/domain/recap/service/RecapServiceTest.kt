package com.retoday.core.domain.recap.service

import com.retoday.core.common.ServiceTest
import com.retoday.core.domain.history.dto.query.GetMyCategoryAnalysisQuery
import com.retoday.core.domain.history.repository.HistoryRepository
import com.retoday.core.domain.history.service.HistoryService
import com.retoday.core.domain.recap.client.RecapAIClient
import com.retoday.core.domain.recap.component.ImagePolicyResolver
import com.retoday.core.domain.recap.component.RecapType
import com.retoday.core.domain.recap.dto.request.GenerateRecapRequest
import com.retoday.core.domain.recap.dto.request.RecapPayload
import com.retoday.core.domain.recap.dto.response.GeminiRecapResponse
import com.retoday.core.domain.recap.dto.response.GeminiTimelineResponse
import com.retoday.core.domain.recap.dto.response.GeminiTopicResponse
import com.retoday.core.domain.recap.entity.Section
import com.retoday.core.domain.recap.entity.Timeline
import com.retoday.core.domain.recap.entity.Topic
import com.retoday.core.domain.recap.repository.RecapRepository
import com.retoday.core.domain.recap.repository.SectionRepository
import com.retoday.core.domain.recap.repository.TimelineRepository
import com.retoday.core.domain.recap.repository.TopicRepository
import com.retoday.core.domain.user.repository.ProfileRepository
import com.retoday.core.fixture.*
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import java.time.Duration
import java.time.LocalDate

class RecapServiceTest : ServiceTest() {
    private val recapAIClient = mockk<RecapAIClient>()
    private val recapRepository = mockk<RecapRepository>()
    private val sectionRepository = mockk<SectionRepository>()
    private val topicRepository = mockk<TopicRepository>()
    private val timelineRepository = mockk<TimelineRepository>()
    private val historyRepository = mockk<HistoryRepository>()
    private val historyService = mockk<HistoryService>()
    private val profileRepository = mockk<ProfileRepository>()
    private val imagePolicyResolver = mockk<ImagePolicyResolver>()

    private val recapService =
        RecapService(
            recapAIClient = recapAIClient,
            recapRepository = recapRepository,
            sectionRepository = sectionRepository,
            topicRepository = topicRepository,
            timelineRepository = timelineRepository,
            historyRepository = historyRepository,
            historyService = historyService,
            profileRepository = profileRepository,
            imagePolicyResolver = imagePolicyResolver,
            transactionManager = transactionManager
        )

    init {
        Given("사용자가 특정 날짜에 활동 기록을 가지고 있을 때") {
            val userId = ID
            val date = LocalDate.parse("2026-02-23")
            val profile =
                createProfile(
                    userId = userId,
                    firstName = "민주"
                )
            val startedAt = date.atStartOfDay(profile.timeZone.id).toInstant()
            val endedAt = startedAt.plus(Duration.ofDays(1))
            val activities = createUserActivities()
            val activityRequests = createUserActivityRequests(activities)
            val timelineActivities = createUserTimelineActivities()
            val timelineRequests = createUserTimelineRequests(timelineActivities)

            // DB 조회 Mocking
            every { recapRepository.existsByUserIdAndRecapDate(userId, date) } returns false
            every { historyRepository.findUserActivitiesForRecap(userId, startedAt, endedAt) } returns activities
            every { historyRepository.findUserTimelinesForRecap(userId, startedAt, endedAt) } returns timelineActivities
            every { profileRepository.findByUserId(userId) } returns
                createProfile(
                    userId = userId,
                    firstName = profile.firstName
                )
            every {
                historyService.getMyCategoryAnalyses(
                    userId = userId,
                    query = GetMyCategoryAnalysisQuery(date = date)
                )
            } returns createGetMyCategoryAnalysisResult(date)

            // AI 응답 Mocking
            val recapResponse = createGeminiRecapResponse()
            val topicResponse = createGeminiTopicResponse()
            val timelineResponse = createGeminiTimelineResponse()

            every { recapAIClient.modelName } returns "gemini-pro"
            every {
                imagePolicyResolver.resolveImageUrl(
                    userId = userId,
                    recapStartedAt = any(),
                    zoneId = any(),
                    topCategoryName = any(),
                    activities = activities
                )
            } returns "images/11.png"
            every {
                recapAIClient.generate(
                    GenerateRecapRequest(
                        type = RecapType.TODAY_RECAP,
                        nickname = profile.firstName,
                        payload = RecapPayload.Activities(activityRequests)
                    ),
                    GeminiRecapResponse::class.java
                )
            } returns recapResponse
            every {
                recapAIClient.generate(
                    GenerateRecapRequest(
                        type = RecapType.TOPIC,
                        nickname = profile.firstName,
                        payload = RecapPayload.Activities(activityRequests)
                    ),
                    GeminiTopicResponse::class.java
                )
            } returns topicResponse
            every {
                recapAIClient.generate(
                    GenerateRecapRequest(
                        type = RecapType.TIMELINE,
                        nickname = profile.firstName,
                        payload = RecapPayload.Timelines(timelineRequests)
                    ),
                    GeminiTimelineResponse::class.java
                )
            } returns timelineResponse

            // Entity 저장 Mocking
            val savedRecap = createRecap(id = 100L)
            every { recapRepository.save(any()) } returns savedRecap
            every { sectionRepository.saveAll(any<List<Section>>()) } returns emptyList()
            every { topicRepository.saveAll(any<List<Topic>>()) } returns emptyList()
            every { timelineRepository.saveAll(any<List<Timeline>>()) } returns emptyList()

            When("createDailyRecap을 호출하여 리캡 생성을 수행하면") {
                recapService.createDailyRecap(userId, date)

                Then("부모 Recap이 저장되고, 파생되는 모든 엔티티들이 recapId(100L)를 가지고 저장된다") {
                    // 1. 부모 리캡 저장 확인
                    verify(exactly = 1) { recapRepository.save(any()) }

                    // 2. 섹션 저장 확인
                    verify(exactly = 1) {
                        sectionRepository.saveAll(
                            match<List<Section>> { sections ->
                                // 타입을 명시적으로 지정
                                sections.all { it.recapId == 100L }
                            }
                        )
                    }

                    // 3. 토픽 저장 확인
                    verify(exactly = 1) {
                        topicRepository.saveAll(
                            match<List<Topic>> { topics ->
                                // 타입을 명시적으로 지정
                                topics.all { it.recapId == 100L }
                            }
                        )
                    }

                    // 4. 타임라인 저장 확인
                    verify(exactly = 1) {
                        timelineRepository.saveAll(
                            match<List<Timeline>> { timelines ->
                                // 타입을 명시적으로 지정
                                timelines.all { it.recapId == 100L }
                            }
                        )
                    }
                }
            }
        }
    }
}
