package com.retoday.core.global.config

import com.retoday.core.domain.history.entity.Website
import com.retoday.core.domain.history.entity.WebsiteCategory
import com.retoday.core.domain.history.entity.WebsiteCategoryCode
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
            WebsiteCategoryCode.entries.map { code ->
                WebsiteCategory(code = code, name = code.defaultName)
            }

        websiteCategoryRepository.saveAll(categories)
    }

    private fun initializeWebsites() {
        if (websiteRepository.count() > 0) return

        val categoryMap = websiteCategoryRepository.findAll().associateBy { it.code }

        fun categoryId(code: WebsiteCategoryCode) = categoryMap[code]?.id!!

        val websites =
            listOf(
                // 학습
                Website(domain = "coursera.org", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "udemy.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "khanacademy.org", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "edx.org", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "class101.net", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "inflearn.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "fastcampus.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "coloso.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "hackers.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "megastudy.net", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "ebsi.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "ringleplus.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "willbes.net", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "gongdangi.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "megagong.net", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "daebak.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "ybm.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "pagoda21.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "champstudy.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "ets.org", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "leetcode.com", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                Website(domain = "programmers.co.kr", categoryId = categoryId(WebsiteCategoryCode.STUDY)),
                // 쇼핑
                Website(domain = "coupang.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "11st.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "gmarket.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "auction.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "ssg.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "musinsa.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "zigzag.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "todayshouse.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "aliexpress.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "29cm.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "temu.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "shein.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "ably.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "wconcept.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "lfmall.co.kr", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "hm.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "zara.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "uniqlo.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "amazon.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "shopping.naver.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "smartstore.naver.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "m.smartstore.naver.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                Website(domain = "brand.naver.com", categoryId = categoryId(WebsiteCategoryCode.SHOPPING)),
                // 게임
                Website(domain = "store.steampowered.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "playstation.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "xbox.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "epicgames.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "leagueoflegends.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "battlenet.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "nexon.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "plaync.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                Website(domain = "onstove.com", categoryId = categoryId(WebsiteCategoryCode.GAMING)),
                // 콘텐츠
                Website(domain = "netflix.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "youtube.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "tving.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "watcha.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "wavve.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "disneyplus.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "spotify.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "webtoons.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "series.naver.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "page.kakao.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "webtoon.kakao.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "ridibooks.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "ridi.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "munpia.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "novel.munpia.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "joara.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "blog.naver.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                Website(domain = "m.blog.naver.com", categoryId = categoryId(WebsiteCategoryCode.CONTENT)),
                // 커뮤니티
                Website(domain = "instagram.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "facebook.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "x.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "reddit.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "theqoo.net", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "dcinside.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "clien.net", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "blind.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "fmkorea.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "instiz.net", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "tiktok.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "discord.com", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "telegram.org", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                Website(domain = "velog.io", categoryId = categoryId(WebsiteCategoryCode.COMMUNITY)),
                // 뉴스/시사
                Website(domain = "news.naver.com", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "news.daum.net", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "joongang.co.kr", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "chosun.com", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "hani.co.kr", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "kmib.co.kr", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "mk.co.kr", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "yonhapnews.co.kr", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "bbc.com", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                Website(domain = "cnn.com", categoryId = categoryId(WebsiteCategoryCode.NEWS)),
                // 금융/자산
                Website(domain = "toss.im", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "kakaobank.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "kbfg.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "shinhancard.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "shinhan.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "wooribank.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "hanafn.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "nhbank.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "kis.co.kr", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "kr.investing.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                Website(domain = "upbit.com", categoryId = categoryId(WebsiteCategoryCode.FINANCE)),
                // 생활/편의
                Website(domain = "mail.google.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "calendar.google.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "drive.google.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "docs.google.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "notion.so", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "map.naver.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "map.kakao.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "baemin.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "yogiyo.co.kr", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "catchtable.co.kr", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "booking.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                Website(domain = "airbnb.com", categoryId = categoryId(WebsiteCategoryCode.LIFESTYLE)),
                // 웹서핑
                Website(domain = "namu.wiki", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "google.com", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "bing.com", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "duckduckgo.com", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "naver.com", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "daum.net", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                Website(domain = "wikipedia.org", categoryId = categoryId(WebsiteCategoryCode.BROWSING)),
                // 디자인
                Website(domain = "figma.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "behance.net", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "dribbble.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "pinterest.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "unsplash.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "pexels.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "adobe.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "canva.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "mobbin.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "proto.io", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                Website(domain = "framer.com", categoryId = categoryId(WebsiteCategoryCode.DESIGN)),
                // 개발
                Website(domain = "github.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "gitlab.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "stackoverflow.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "dev.to", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "npmjs.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "pypi.org", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "hub.docker.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "kubernetes.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "docs.spring.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "developer.mozilla.org", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "w3schools.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "codepen.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "replit.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "codesandbox.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "jetbrains.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "code.visualstudio.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "postman.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "swagger.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "vercel.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "netlify.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "aws.amazon.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "cloud.google.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "portal.azure.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "terraform.io", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "confluence.atlassian.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                Website(domain = "jira.atlassian.com", categoryId = categoryId(WebsiteCategoryCode.DEVELOPMENT)),
                // AI
                Website(domain = "chatgpt.com", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "openai.com", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "claude.ai", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "anthropic.com", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "gemini.google.com", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "poe.com", categoryId = categoryId(WebsiteCategoryCode.AI)),
                Website(domain = "perplexity.ai", categoryId = categoryId(WebsiteCategoryCode.AI))
            )

        websiteRepository.saveAll(websites)
    }
}
