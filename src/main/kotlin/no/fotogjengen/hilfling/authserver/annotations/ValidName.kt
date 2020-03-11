package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomNameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented // annotation is part of a public API
@Retention(AnnotationRetention.RUNTIME) // Invoke on runtime
@Target(AnnotationTarget.FIELD) // Annotation can be used on fields
@Constraint(validatedBy = [CustomNameValidator::class]) // Validated by CustomNameValidator
annotation class ValidName (
        val message: String = "Name must be at least 2 characters long",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)