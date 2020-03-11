package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomNameValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [CustomNameValidator::class])
annotation class ValidName (
        val message: String = "Name must be at least 2 characters long",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)