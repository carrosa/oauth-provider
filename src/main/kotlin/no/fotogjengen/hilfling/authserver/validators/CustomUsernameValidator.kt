package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidUsername
import no.fotogjengen.hilfling.authserver.services.UserService
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomUsernameValidator(
        private val userService: UserService
) : ConstraintValidator<ValidUsername, String> {

    private val MINIMUM_USERNAME_LENGTH = 4

    override fun isValid(username: String, context: ConstraintValidatorContext): Boolean {
        val messages = ArrayList<String>()
        if (username.length < MINIMUM_USERNAME_LENGTH) {
            messages.add(String.format("Username must me at least %s characters long", MINIMUM_USERNAME_LENGTH))
        }
        if (userService.isUsernameAlreadyInUse(username)) {
            messages.add("Username already exists")
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