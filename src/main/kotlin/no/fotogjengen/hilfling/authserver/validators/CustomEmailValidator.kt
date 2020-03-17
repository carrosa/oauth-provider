package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidEmail
import no.fotogjengen.hilfling.authserver.services.WhitelistedEmailDomainService
import no.fotogjengen.hilfling.authserver.services.WhitelistedEmailService
import no.fotogjengen.hilfling.authserver.services.UserService
import java.util.regex.Pattern
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext

/*
* Validator for email, invoked by @ValidEmail annotation
* */
class CustomEmailValidator(
        private val userService: UserService,
        private val whitelistedEmailService: WhitelistedEmailService,
        private val whitelistedEmailDomainService: WhitelistedEmailDomainService
) : ConstraintValidator<ValidEmail, String> {

    // Regex pattern to validate email, this pattern is according to RFC 5322
    private val EMAIL_PATTERN_RFC5322 = "(?:[a-z0-9!#\$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#\$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])"

    init {

    }

    override fun isValid(email: String, context: ConstraintValidatorContext): Boolean {
        /*
        * @param email, Email to validate
        * @param context, Context passed back to frontend template with constraint messages
        * @return Boolean, is the email valid or not
        * */

        // Skip other validation if email is whitelisted
        if (whitelistedEmailService.isEmailWhitelisted(email)) return true
        val pattern = Pattern.compile(EMAIL_PATTERN_RFC5322)
        val matcher = pattern.matcher(email)
        val messages = ArrayList<String>() // Error messages passed to context (frontend)
        if (!whitelistedEmailDomainService.isEmailDomainWhitelisted(email.split("@")[1])) {
            /*
            * If email domain is not whitelisted, then add error message
            * */
            messages.add(String.format("Email address must end in: %s",
                    whitelistedEmailDomainService.getWhitelistedEmailDomains().joinToString(", "))
            )
        }
        if (userService.isEmailAlreadyInUse(email)) {
            // If email is in use
            messages.add("Email is already in use")
        }
        if (!matcher.matches()) {
            // Email does not match regex pattern
            messages.add("Email address must be a valid email")
        }
        if (messages.size < 1) {
            // No error messages, so we return true
            return true
        }

        // Build context with messages, separated with ";" to make it easy to map to a list in the frontend template
        val messageTemplate = messages.stream()
                .collect(Collectors.joining(";"))
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation()
        return false // return false, since we have at least 1 error message
    }
}