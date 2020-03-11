package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomUsernameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented // annotation is part of a public API
@Retention(AnnotationRetention.RUNTIME) // Invoke on runtime
@Target(AnnotationTarget.FIELD) // Annotation can be used on fields
@Constraint(validatedBy = [CustomUsernameValidator::class]) // Validated by CustomUsernameValidator
annotation class ValidUsername (
        val message: String = "Invalid username",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)