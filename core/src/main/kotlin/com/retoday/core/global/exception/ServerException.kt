package com.retoday.core.global.exception

abstract class ServerException(
    val status: Int,
    override val message: String
) : RuntimeException(message)
