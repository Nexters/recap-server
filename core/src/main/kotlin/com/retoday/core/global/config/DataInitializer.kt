package com.retoday.core.global.config

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.repository.WebsiteCategoryRepository
import com.retoday.core.domain.history.repository.WebsiteRepository
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class DataInitializer(
    private val websiteCategoryRepository: WebsiteCategoryRepository,
    private val websiteRepository: WebsiteRepository
) : ApplicationRunner {
    @Transactional
    override fun run(args: ApplicationArguments) {
        initializeWebsiteCategories()
        initializeWebsites()
    }

    private fun initializeWebsiteCategories() {
        if (websiteCategoryRepository.count() > 0) return

        val categories =
            listOf(
                WebsiteCategory(name = "학습"),
                WebsiteCategory(name = "쇼핑"),
                WebsiteCategory(name = "게임"),
                WebsiteCategory(name = "콘텐츠"),
                WebsiteCategory(name = "커뮤니티"),
                WebsiteCategory(name = "뉴스/시사"),
                WebsiteCategory(name = "금융/자산"),
                WebsiteCategory(name = "생활/편의"),
                WebsiteCategory(name = "웹서핑"),
                WebsiteCategory(name = "디자인"),
                WebsiteCategory(name = "개발"),
                WebsiteCategory(name = "AI"),
                WebsiteCategory(name = "기타")
            )

        websiteCategoryRepository.saveAll(categories)
    }

    private fun initializeWebsites() {
        if (websiteRepository.count() > 0) return

        val categoryMap = websiteCategoryRepository.findAll().associateBy { it.name }

        fun categoryId(name: String) = categoryMap[name]?.id!!

        val websites =
            listOf(
                // 학습
                Website(domain = "coursera.org", categoryId = categoryId("학습")),
                Website(domain = "udemy.com", categoryId = categoryId("학습")),
                Website(domain = "khanacademy.org", categoryId = categoryId("학습")),
                Website(domain = "edx.org", categoryId = categoryId("학습")),
                Website(domain = "class101.net", categoryId = categoryId("학습")),
                Website(domain = "inflearn.com", categoryId = categoryId("학습")),
                Website(domain = "fastcampus.co.kr", categoryId = categoryId("학습")),
                Website(domain = "coloso.co.kr", categoryId = categoryId("학습")),
                Website(domain = "hackers.com", categoryId = categoryId("학습")),
                Website(domain = "megastudy.net", categoryId = categoryId("학습")),
                Website(domain = "ebsi.co.kr", categoryId = categoryId("학습")),
                Website(domain = "ringleplus.com", categoryId = categoryId("학습")),
                Website(domain = "willbes.net", categoryId = categoryId("학습")),
                Website(domain = "gongdangi.com", categoryId = categoryId("학습")),
                Website(domain = "megagong.net", categoryId = categoryId("학습")),
                Website(domain = "daebak.co.kr", categoryId = categoryId("학습")),
                Website(domain = "ybm.co.kr", categoryId = categoryId("학습")),
                Website(domain = "pagoda21.com", categoryId = categoryId("학습")),
                Website(domain = "champstudy.com", categoryId = categoryId("학습")),
                Website(domain = "ets.org", categoryId = categoryId("학습")),
                Website(domain = "leetcode.com", categoryId = categoryId("학습")),
                Website(domain = "programmers.co.kr", categoryId = categoryId("학습")),
                // 쇼핑
                Website(domain = "coupang.com", categoryId = categoryId("쇼핑")),
                Website(domain = "11st.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "gmarket.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "auction.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "ssg.com", categoryId = categoryId("쇼핑")),
                Website(domain = "musinsa.com", categoryId = categoryId("쇼핑")),
                Website(domain = "zigzag.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "todayshouse.com", categoryId = categoryId("쇼핑")),
                Website(domain = "aliexpress.com", categoryId = categoryId("쇼핑")),
                Website(domain = "29cm.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "temu.com", categoryId = categoryId("쇼핑")),
                Website(domain = "shein.com", categoryId = categoryId("쇼핑")),
                Website(domain = "ably.com", categoryId = categoryId("쇼핑")),
                Website(domain = "wconcept.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "lfmall.co.kr", categoryId = categoryId("쇼핑")),
                Website(domain = "hm.com", categoryId = categoryId("쇼핑")),
                Website(domain = "zara.com", categoryId = categoryId("쇼핑")),
                Website(domain = "uniqlo.com", categoryId = categoryId("쇼핑")),
                Website(domain = "amazon.com", categoryId = categoryId("쇼핑")),
                Website(domain = "shopping.naver.com", categoryId = categoryId("쇼핑")),
                Website(domain = "smartstore.naver.com", categoryId = categoryId("쇼핑")),
                Website(domain = "m.smartstore.naver.com", categoryId = categoryId("쇼핑")),
                Website(domain = "brand.naver.com", categoryId = categoryId("쇼핑")),
                // 게임
                Website(domain = "store.steampowered.com", categoryId = categoryId("게임")),
                Website(domain = "playstation.com", categoryId = categoryId("게임")),
                Website(domain = "xbox.com", categoryId = categoryId("게임")),
                Website(domain = "epicgames.com", categoryId = categoryId("게임")),
                Website(domain = "leagueoflegends.com", categoryId = categoryId("게임")),
                Website(domain = "battlenet.com", categoryId = categoryId("게임")),
                Website(domain = "nexon.com", categoryId = categoryId("게임")),
                Website(domain = "plaync.com", categoryId = categoryId("게임")),
                Website(domain = "onstove.com", categoryId = categoryId("게임")),
                // 콘텐츠
                Website(domain = "netflix.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "youtube.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "tving.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "watcha.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "wavve.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "disneyplus.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "spotify.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "webtoons.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "series.naver.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "page.kakao.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "webtoon.kakao.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "ridibooks.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "ridi.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "munpia.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "novel.munpia.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "joara.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "blog.naver.com", categoryId = categoryId("콘텐츠")),
                Website(domain = "m.blog.naver.com", categoryId = categoryId("콘텐츠")),
                // 커뮤니티
                Website(domain = "instagram.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "facebook.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "x.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "reddit.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "theqoo.net", categoryId = categoryId("커뮤니티")),
                Website(domain = "dcinside.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "clien.net", categoryId = categoryId("커뮤니티")),
                Website(domain = "blind.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "fmkorea.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "instiz.net", categoryId = categoryId("커뮤니티")),
                Website(domain = "tiktok.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "discord.com", categoryId = categoryId("커뮤니티")),
                Website(domain = "telegram.org", categoryId = categoryId("커뮤니티")),
                Website(domain = "velog.io", categoryId = categoryId("커뮤니티")),
                // 뉴스/시사
                Website(domain = "news.naver.com", categoryId = categoryId("뉴스/시사")),
                Website(domain = "news.daum.net", categoryId = categoryId("뉴스/시사")),
                Website(domain = "joongang.co.kr", categoryId = categoryId("뉴스/시사")),
                Website(domain = "chosun.com", categoryId = categoryId("뉴스/시사")),
                Website(domain = "hani.co.kr", categoryId = categoryId("뉴스/시사")),
                Website(domain = "kmib.co.kr", categoryId = categoryId("뉴스/시사")),
                Website(domain = "mk.co.kr", categoryId = categoryId("뉴스/시사")),
                Website(domain = "yonhapnews.co.kr", categoryId = categoryId("뉴스/시사")),
                Website(domain = "bbc.com", categoryId = categoryId("뉴스/시사")),
                Website(domain = "cnn.com", categoryId = categoryId("뉴스/시사")),
                // 금융/자산
                Website(domain = "toss.im", categoryId = categoryId("금융/자산")),
                Website(domain = "kakaobank.com", categoryId = categoryId("금융/자산")),
                Website(domain = "kbfg.com", categoryId = categoryId("금융/자산")),
                Website(domain = "shinhancard.com", categoryId = categoryId("금융/자산")),
                Website(domain = "shinhan.com", categoryId = categoryId("금융/자산")),
                Website(domain = "wooribank.com", categoryId = categoryId("금융/자산")),
                Website(domain = "hanafn.com", categoryId = categoryId("금융/자산")),
                Website(domain = "nhbank.com", categoryId = categoryId("금융/자산")),
                Website(domain = "kis.co.kr", categoryId = categoryId("금융/자산")),
                Website(domain = "kr.investing.com", categoryId = categoryId("금융/자산")),
                Website(domain = "upbit.com", categoryId = categoryId("금융/자산")),
                // 생활/편의
                Website(domain = "mail.google.com", categoryId = categoryId("생활/편의")),
                Website(domain = "calendar.google.com", categoryId = categoryId("생활/편의")),
                Website(domain = "drive.google.com", categoryId = categoryId("생활/편의")),
                Website(domain = "docs.google.com", categoryId = categoryId("생활/편의")),
                Website(domain = "notion.so", categoryId = categoryId("생활/편의")),
                Website(domain = "map.naver.com", categoryId = categoryId("생활/편의")),
                Website(domain = "map.kakao.com", categoryId = categoryId("생활/편의")),
                Website(domain = "baemin.com", categoryId = categoryId("생활/편의")),
                Website(domain = "yogiyo.co.kr", categoryId = categoryId("생활/편의")),
                Website(domain = "catchtable.co.kr", categoryId = categoryId("생활/편의")),
                Website(domain = "booking.com", categoryId = categoryId("생활/편의")),
                Website(domain = "airbnb.com", categoryId = categoryId("생활/편의")),
                // 웹서핑
                Website(domain = "namu.wiki", categoryId = categoryId("웹서핑")),
                Website(domain = "google.com", categoryId = categoryId("웹서핑")),
                Website(domain = "bing.com", categoryId = categoryId("웹서핑")),
                Website(domain = "duckduckgo.com", categoryId = categoryId("웹서핑")),
                Website(domain = "naver.com", categoryId = categoryId("웹서핑")),
                Website(domain = "daum.net", categoryId = categoryId("웹서핑")),
                Website(domain = "wikipedia.org", categoryId = categoryId("웹서핑")),
                // 디자인
                Website(domain = "figma.com", categoryId = categoryId("디자인")),
                Website(domain = "behance.net", categoryId = categoryId("디자인")),
                Website(domain = "dribbble.com", categoryId = categoryId("디자인")),
                Website(domain = "pinterest.com", categoryId = categoryId("디자인")),
                Website(domain = "unsplash.com", categoryId = categoryId("디자인")),
                Website(domain = "pexels.com", categoryId = categoryId("디자인")),
                Website(domain = "adobe.com", categoryId = categoryId("디자인")),
                Website(domain = "canva.com", categoryId = categoryId("디자인")),
                Website(domain = "mobbin.com", categoryId = categoryId("디자인")),
                Website(domain = "proto.io", categoryId = categoryId("디자인")),
                Website(domain = "framer.com", categoryId = categoryId("디자인")),
                // 개발
                Website(domain = "github.com", categoryId = categoryId("개발")),
                Website(domain = "gitlab.com", categoryId = categoryId("개발")),
                Website(domain = "stackoverflow.com", categoryId = categoryId("개발")),
                Website(domain = "dev.to", categoryId = categoryId("개발")),
                Website(domain = "npmjs.com", categoryId = categoryId("개발")),
                Website(domain = "pypi.org", categoryId = categoryId("개발")),
                Website(domain = "hub.docker.com", categoryId = categoryId("개발")),
                Website(domain = "kubernetes.io", categoryId = categoryId("개발")),
                Website(domain = "docs.spring.io", categoryId = categoryId("개발")),
                Website(domain = "developer.mozilla.org", categoryId = categoryId("개발")),
                Website(domain = "w3schools.com", categoryId = categoryId("개발")),
                Website(domain = "codepen.io", categoryId = categoryId("개발")),
                Website(domain = "replit.com", categoryId = categoryId("개발")),
                Website(domain = "codesandbox.io", categoryId = categoryId("개발")),
                Website(domain = "jetbrains.com", categoryId = categoryId("개발")),
                Website(domain = "code.visualstudio.com", categoryId = categoryId("개발")),
                Website(domain = "postman.com", categoryId = categoryId("개발")),
                Website(domain = "swagger.io", categoryId = categoryId("개발")),
                Website(domain = "vercel.com", categoryId = categoryId("개발")),
                Website(domain = "netlify.com", categoryId = categoryId("개발")),
                Website(domain = "aws.amazon.com", categoryId = categoryId("개발")),
                Website(domain = "cloud.google.com", categoryId = categoryId("개발")),
                Website(domain = "portal.azure.com", categoryId = categoryId("개발")),
                Website(domain = "terraform.io", categoryId = categoryId("개발")),
                Website(domain = "confluence.atlassian.com", categoryId = categoryId("개발")),
                Website(domain = "jira.atlassian.com", categoryId = categoryId("개발")),
                // AI
                Website(domain = "chatgpt.com", categoryId = categoryId("AI")),
                Website(domain = "openai.com", categoryId = categoryId("AI")),
                Website(domain = "claude.ai", categoryId = categoryId("AI")),
                Website(domain = "anthropic.com", categoryId = categoryId("AI")),
                Website(domain = "gemini.google.com", categoryId = categoryId("AI")),
                Website(domain = "poe.com", categoryId = categoryId("AI")),
                Website(domain = "perplexity.ai", categoryId = categoryId("AI"))
            )

        websiteRepository.saveAll(websites)
    }
}
