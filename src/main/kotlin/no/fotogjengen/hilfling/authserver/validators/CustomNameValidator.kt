package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidName
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomNameValidator : ConstraintValidator<ValidName, String> {
    private val MINIMUM_NAME_LENGTH = 2

    override fun isValid(name: String, context: ConstraintValidatorContext): Boolean {
        val messages = ArrayList<String>()
        if (name.length < 2) {
            messages.add(String.format("Name must be at least %s characters long", MINIMUM_NAME_LENGTH))
        }
        if (messages.size < 1) {
            return true
        }
        val messageTemplate = messages.stream()
                .collect(Collectors.joining(";"))
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation()
        return false
    }
}