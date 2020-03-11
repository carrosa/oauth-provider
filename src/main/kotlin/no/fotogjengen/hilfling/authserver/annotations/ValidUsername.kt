package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomUsernameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [CustomUsernameValidator::class])
annotation class ValidUsername (
        val message: String = "Invalid username",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)