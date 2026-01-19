package com.recap.core.global.exception

abstract class ServerException(
    val status: Int,
    override val message: String
) : RuntimeException(message)
