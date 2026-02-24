package com.retoday.core.domain.recap.component

object ImagePolicyMapper {
    private val imageMap =
        mapOf(
            ImagePolicyType.IMAGE_01_LEARNING_TOP to "01.png",
            ImagePolicyType.IMAGE_02_SHOPPING_TOP to "02.png",
            ImagePolicyType.IMAGE_03_GAME_TOP to "03.png",
            ImagePolicyType.IMAGE_04_CONTENT_TOP to "04.png",
            ImagePolicyType.IMAGE_05_COMMUNITY_TOP to "05.png",
            ImagePolicyType.IMAGE_06_NEWS_TOP to "06.png",
            ImagePolicyType.IMAGE_07_FINANCE_TOP to "07.png",
            ImagePolicyType.IMAGE_08_LIFE_TOP to "08.png",
            ImagePolicyType.IMAGE_09_SURFING_TOP to "09.png",
            ImagePolicyType.IMAGE_10_DESIGN_TOP to "10.png",
            ImagePolicyType.IMAGE_11_DEVELOPMENT_TOP to "11.png",
            ImagePolicyType.IMAGE_12_SCREEN_TIME_OVER_12H to "12.png",
            ImagePolicyType.IMAGE_13_SCREEN_TIME_UNDER_1H to "13.png",
            ImagePolicyType.IMAGE_14_CATEGORY_OVER_5 to "14.png",
            ImagePolicyType.IMAGE_15_CATEGORY_ONLY_1 to "15.png",
            ImagePolicyType.IMAGE_16_START_AFTER_9PM to "16.png",
            ImagePolicyType.IMAGE_17_START_BEFORE_9AM to "17.png",
            ImagePolicyType.IMAGE_18_RANDOM_1 to "18.png",
            ImagePolicyType.IMAGE_19_RANDOM_2 to "19.png",
            ImagePolicyType.IMAGE_20_RANDOM_3 to "20.png"
        )

    fun toImageFileName(policyType: ImagePolicyType): String = requireNotNull(imageMap[policyType])
}
