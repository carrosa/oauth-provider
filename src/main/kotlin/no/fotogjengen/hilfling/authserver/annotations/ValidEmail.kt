package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomEmailValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass

@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [CustomEmailValidator::class])
annotation class ValidEmail(
        val message: String = "Invalid email",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)