package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidName
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/*
* Validator for names, invoked by @ValidName annotation
* */
class CustomNameValidator : ConstraintValidator<ValidName, String> {
    private val MINIMUM_NAME_LENGTH = 2 // Name has to be at least 2 characters long

    override fun isValid(name: String, context: ConstraintValidatorContext): Boolean {
        /*
       * @param name, Name to validate
       * @param context, Context passed back to frontend template with constraint messages
       * @return Boolean, Is the name valid or not
       * */
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