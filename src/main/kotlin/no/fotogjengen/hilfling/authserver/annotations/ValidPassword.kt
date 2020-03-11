package no.fotogjengen.hilfling.authserver.annotations

import no.fotogjengen.hilfling.authserver.validators.CustomPasswordValidator
import javax.validation.Constraint
import javax.validation.Payload
import kotlin.reflect.KClass


@MustBeDocumented
@Retention(AnnotationRetention.RUNTIME)
@Target(AnnotationTarget.TYPE, AnnotationTarget.FIELD, AnnotationTarget.ANNOTATION_CLASS)
@Constraint(validatedBy = [CustomPasswordValidator::class])
annotation class ValidPassword (
        val message: String = "Invalid password",
        val groups: Array<KClass<*>> = [],
        val payload: Array<KClass<out Payload>> = []
)