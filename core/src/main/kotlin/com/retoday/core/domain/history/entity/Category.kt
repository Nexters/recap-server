package com.retoday.core.domain.history.entity

enum class Category(
    val displayName: String
) {
    STUDY("학습"),
    SHOPPING("쇼핑"),
    GAME("게임"),
    CONTENT("콘텐츠"),
    COMMUNITY("커뮤니티"),
    NEWS("뉴스/시사"),
    FINANCE("금융/자산"),
    LIFE("생활/편의"),
    SURFING("웹서핑"),
    DESIGN("디자인"),
    DEVELOPMENT("개발"),
    ETC("기타");

    companion object {
        fun fromDisplayName(name: String): Category? = values().firstOrNull { it.displayName == name }
    }
}
