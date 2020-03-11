package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidEmail
import no.fotogjengen.hilfling.authserver.services.WhitelistedEmailDomainService
import no.fotogjengen.hilfling.authserver.services.WhitelistedEmailService
import no.fotogjengen.hilfling.authserver.services.UserService
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

class CustomEmailValidator(
        private val userService: UserService,
        private val whitelistedEmailService: WhitelistedEmailService,
        private val whitelistedEmailDomainService: WhitelistedEmailDomainService
) : ConstraintValidator<ValidEmail, String> {

    private val EMAIL_PATTERN_RFC5322 = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    override fun isValid(email: String, context: ConstraintValidatorContext): Boolean {
        if (whitelistedEmailService.isEmailWhitelisted(email)) return true
        val pattern = Pattern.compile(EMAIL_PATTERN_RFC5322)
        val matcher = pattern.matcher(email)
        val messages = ArrayList<String>()
        if (!whitelistedEmailDomainService.isEmailDomainWhitelisted(email.split("@")[1])) {
            messages.add(String.format("Email address must end in: %s",
                    whitelistedEmailDomainService.getWhitelistedEmailDomains().joinToString(", "))
            )
        }
        if (userService.isEmailAlreadyInUse(email)) {
            messages.add("Email is already in use")
        }
        if (!matcher.matches()) {
            messages.add("Email address must be a valid email")
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