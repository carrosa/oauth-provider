package no.fotogjengen.hilfling.authserver.validators

import no.fotogjengen.hilfling.authserver.annotations.ValidPassword
import org.passay.*
import java.util.stream.Collectors
import javax.validation.ConstraintValidator
import javax.validation.ConstraintValidatorContext


/*
* Validator for passwords, invoked by @ValidPassword annotation
* */
class CustomPasswordValidator : ConstraintValidator<ValidPassword, String> {

    override fun isValid(password: String, context: ConstraintValidatorContext): Boolean {
        /*
       * @param password, Password to validate
       * @param context, Context passed back to frontend template with constraint messages
       * @return Boolean, Is the password valid or not
       * */
        val validator = PasswordValidator(
                arrayListOf(
                        LengthRule(8, 300),
                        CharacterRule(EnglishCharacterData.UpperCase, 1),
                        CharacterRule(EnglishCharacterData.LowerCase, 1),
                        CharacterRule(EnglishCharacterData.Digit, 1),
                        CharacterRule(EnglishCharacterData.Special, 1),
                        WhitespaceRule() // no whitespace
                )
        )
        val result = validator.validate(PasswordData(password))
        if (result.isValid) {
            return true
        }
        val messages = validator.getMessages(result)
        val messageTemplate = messages.stream()
                .collect(Collectors.joining(";"))
        context.buildConstraintViolationWithTemplate(messageTemplate)
                .addConstraintViolation()
                .disableDefaultConstraintViolation()
        return false
    }
}