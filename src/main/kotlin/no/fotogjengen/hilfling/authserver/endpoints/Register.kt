package no.fotogjengen.hilfling.authserver.endpoints

import no.fotogjengen.hilfling.authserver.dtos.UserDTO
import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.enums.Role
import no.fotogjengen.hilfling.authserver.services.UserService
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.validation.Errors
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.context.request.WebRequest
import org.springframework.web.servlet.ModelAndView
import javax.validation.Valid

@FrameworkEndpoint
@RequestMapping("/user/register")
class Register(
        private val service: UserService,
        private val passwordEncoder: PasswordEncoder
) {

    @GetMapping
    fun getUserRegistrationForm(request: WebRequest, model: Model): String {
        val user = UserDTO()
        model.addAttribute("user", user)
        return "registration"
    }

    @PostMapping
    fun registerUserAccount(
            @ModelAttribute("user") @Valid user: UserDTO,
            result: BindingResult, request: WebRequest, errors: Errors
    ): ModelAndView {

        return if (!result.hasErrors()) {
            service.registerNewUserAccount(User(user, Role.HUSFOLK, passwordEncoder))
            ModelAndView("successfulRegistration", "user", user)
        } else {
            ModelAndView("registration", "user", user)
        }
    }
}