package com.retoday.core.global.util

import com.retoday.core.domain.history.exception.InvalidUrlException
import java.net.URI
import java.net.URLDecoder
import java.nio.charset.StandardCharsets

object UrlUtils {
    fun extractDomain(url: String): String =
        try {
            URI(url)
                .host
                ?.removePrefix("www.")
                ?: throw InvalidUrlException(url)
        } catch (e: Exception) {
            throw InvalidUrlException(url)
        }

    // URL 정규화 (query-param/fragment 제거)
    fun normalizeUrl(url: String): String =
        try {
            val uri = URI(url)
            val scheme = uri.scheme ?: "https"
            val host = uri.host ?: throw InvalidUrlException(url)
            val path = uri.path?.takeIf { it.isNotBlank() } ?: ""

            "$scheme://$host$path"
        } catch (e: Exception) {
            throw InvalidUrlException(url)
        }

    fun extractPath(url: String): String =
        try {
            URI(url).path ?: ""
        } catch (e: Exception) {
            ""
        }

    fun isValidUrl(url: String): Boolean =
        try {
            val uri = URI(url)
            uri.scheme != null && uri.host != null
        } catch (e: Exception) {
            false
        }

    fun extractQueryParams(url: String): Map<String, String> {
        return try {
            val query = URI(url).query ?: return emptyMap()

            query
                .split("&")
                .mapNotNull { param ->
                    val parts = param.split("=", limit = 2)
                    if (parts.size == 2) {
                        val key = URLDecoder.decode(parts[0], StandardCharsets.UTF_8)
                        val value = URLDecoder.decode(parts[1], StandardCharsets.UTF_8)
                        key to value
                    } else {
                        null
                    }
                }.toMap()
        } catch (e: Exception) {
            emptyMap()
        }
    }
}
