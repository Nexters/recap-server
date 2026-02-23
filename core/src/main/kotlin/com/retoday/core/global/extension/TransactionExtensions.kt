package com.retoday.core.global.extension

import org.springframework.stereotype.Component
import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.support.DefaultTransactionDefinition

@Component
class Transaction(
    private val transactionManager: PlatformTransactionManager
) {
    init {
        internalTransactionManager = transactionManager
    }

    companion object {
        private lateinit var internalTransactionManager: PlatformTransactionManager

        operator fun <T> invoke(
            readOnly: Boolean,
            propagation: Propagation,
            func: () -> T
        ): T {
            val definition =
                DefaultTransactionDefinition()
                    .apply {
                        isReadOnly = readOnly
                        propagationBehavior = propagation.value()
                    }
            val transaction = internalTransactionManager.getTransaction(definition)

            return runCatching(func)
                .onSuccess { internalTransactionManager.commit(transaction) }
                .onFailure { internalTransactionManager.rollback(transaction) }
                .getOrThrow()
        }
    }
}

fun <T> transaction(
    readOnly: Boolean = false,
    propagation: Propagation = Propagation.REQUIRED,
    func: () -> T
): T =
    Transaction(
        readOnly,
        propagation,
        func
    )
