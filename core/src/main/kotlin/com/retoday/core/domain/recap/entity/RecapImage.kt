package com.retoday.core.domain.recap.entity

enum class RecapImage(
    val fileName: String,
    val categoryName: String? = null
) {
    STUDY("01.png", "학습"),
    SHOPPING("02.png", "쇼핑"),
    GAME("03.png", "게임"),
    CONTENT("04.png", "콘텐츠"),
    COMMUNITY("05.png", "커뮤니티"),
    NEWS("06.png", "뉴스/시사"),
    FINANCE("07.png", "금융/자산"),
    LIFE("08.png", "생활/편의"),
    SURFING("09.png", "웹서핑"),
    DESIGN("10.png", "디자인"),
    DEVELOPMENT("11.png", "개발"),
    SCREEN_TIME_OVER_12H("12.png"),
    SCREEN_TIME_UNDER_1H("13.png"),
    CATEGORY_OVER_5("14.png"),
    CATEGORY_ONLY_1("15.png"),
    START_AFTER_9PM("16.png"),
    START_BEFORE_9AM("17.png"),
    RANDOM_1("18.png"),
    RANDOM_2("19.png"),
    RANDOM_3("20.png")
}
