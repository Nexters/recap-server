package com.retoday.core.common

import com.retoday.core.global.extension.transaction
import io.kotest.core.spec.Spec
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockkStatic
import org.springframework.transaction.annotation.Propagation
import kotlin.reflect.KFunction

abstract class ServiceTest : BehaviorSpec() {
    override suspend fun beforeSpec(spec: Spec) {
        val function: (Boolean, Propagation, () -> Any) -> Any = ::transaction
        mockkStatic(function as KFunction<*>)

        every { transaction<Any>(any(), any(), any()) } answers { arg<() -> Any>(2).invoke() }
    }
}
