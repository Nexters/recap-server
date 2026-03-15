package com.retoday.core.domain.history.service

import org.springframework.dao.DuplicateKeyException
import org.springframework.data.relational.core.conversion.DbActionExecutionException
import java.sql.SQLIntegrityConstraintViolationException

internal fun DbActionExecutionException.isDuplicateKeyViolation(): Boolean {
    var cause: Throwable? = this
    while (cause != null) {
        when (cause) {
            is DuplicateKeyException -> {
                return true
            }

            is SQLIntegrityConstraintViolationException -> {
                if (cause.errorCode == 1062) return true
            }
        }
        cause = cause.cause
    }
    return false
}
