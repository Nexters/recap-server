package com.retoday.core.global.extension

import org.springframework.transaction.PlatformTransactionManager
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.support.DefaultTransactionDefinition

fun <T> PlatformTransactionManager.transaction(
    readOnly: Boolean = false,
    propagation: Propagation = Propagation.REQUIRED,
    func: () -> T
): T {
    val definition =
        DefaultTransactionDefinition()
            .apply {
                isReadOnly = readOnly
                propagationBehavior = propagation.value()
            }
    val transaction = getTransaction(definition)

    return runCatching(func)
        .onSuccess { commit(transaction) }
        .onFailure { rollback(transaction) }
        .getOrThrow()
}
