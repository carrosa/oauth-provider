package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomEmailValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented // annotation is part of a public API
@Retention(AnnotationRetention.RUNTIME) // Invoke on runtime
@Target(AnnotationTarget.FIELD) // Annotation can be used on fields
@Constraint(validatedBy = [CustomEmailValidator::class]) // Validated by CustomEmailValidator
annotation class ValidEmail(
        val message: String = "Invalid email",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)