package com.retoday.core.common

import com.retoday.core.global.extension.transaction
import io.kotest.core.spec.style.BehaviorSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import org.springframework.transaction.PlatformTransactionManager

abstract class ServiceTest : BehaviorSpec() {
    private companion object {
        const val TRANSACTION_EXTENSIONS_CLASS = "com.retoday.core.global.extension.TransactionExtensionsKt"
    }

    protected val transactionManager =
        mockk<PlatformTransactionManager>()
            .apply {
                mockkStatic(TRANSACTION_EXTENSIONS_CLASS)
                every { transaction<Any?>(any(), any(), any()) } answers { lastArg<() -> Any?>().invoke() }
            }
}
