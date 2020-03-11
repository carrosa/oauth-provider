package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomPasswordValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented // annotation is part of a public API
@Retention(AnnotationRetention.RUNTIME) // Invoke on runtime
@Target(AnnotationTarget.FIELD) // Annotation can be used on fields
@Constraint(validatedBy = [CustomPasswordValidator::class]) // Validated by CustomPasswordValidator
annotation class ValidPassword (
        val message: String = "Invalid password",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)