package com.retoday.core.domain.history.entity

enum class WebsiteCategoryCode(
    val defaultName: String
) {
    STUDY("학습"),
    SHOPPING("쇼핑"),
    GAMING("게임"),
    CONTENT("콘텐츠"),
    COMMUNITY("커뮤니티"),
    NEWS("뉴스/시사"),
    FINANCE("금융/자산"),
    LIFESTYLE("생활/편의"),
    BROWSING("웹서핑"),
    DESIGN("디자인"),
    DEVELOPMENT("개발"),
    AI("AI"),
    OTHER("기타")
}
