package com.retoday.core.domain.recap.component

import com.retoday.core.domain.recap.exception.RecapPromptNotFoundException
import org.springframework.core.io.ResourceLoader
import org.springframework.stereotype.Component

@Component
class RecapPromptManager(
    private val resourceLoader: ResourceLoader
) {
    fun getDailyRecapPrompt(
        type: RecapType,
        variables: Map<String, String>
    ): String {
        val path = "classpath:prompts/${type.fileName}.md"
        val resource = resourceLoader.getResource(path)
        if (!resource.exists()) {
            throw RecapPromptNotFoundException()
        }

        var content = resource.inputStream.bufferedReader().use { it.readText() }

        variables.forEach { (key, value) ->
            val target = "\${$key}"
            content = content.replace(target, value)
        }

        return content
    }
}

enum class RecapType(
    val fileName: String
) {
    TODAY_RECAP("1-today-recap"),
    TIMELINE("2-timeline"),
    TOPIC("3-topic")
}
