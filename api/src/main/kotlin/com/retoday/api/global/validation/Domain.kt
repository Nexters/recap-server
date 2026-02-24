package com.retoday.api.global.validation

import jakarta.validation.Constraint
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import jakarta.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Target(
    AnnotationTarget.FIELD,
    AnnotationTarget.VALUE_PARAMETER
)
@Retention(AnnotationRetention.RUNTIME)
@Constraint(validatedBy = [DomainValidator::class])
annotation class Domain(
    val message: String = "유효한 도메인 형식이 아닙니다.",
    val groups: Array<KClass<*>> = [],
    val payload: Array<KClass<out Payload>> = []
)

class DomainValidator : ConstraintValidator<Domain, String> {
    private companion object {
        val DOMAIN_REGEX = Regex("^(?!-)(?:[a-zA-Z0-9-]{1,63}\\.)+[a-zA-Z]{2,63}$")
    }

    override fun isValid(
        value: String?,
        context: ConstraintValidatorContext
    ): Boolean = (value == null) || (DOMAIN_REGEX matches value)
}
