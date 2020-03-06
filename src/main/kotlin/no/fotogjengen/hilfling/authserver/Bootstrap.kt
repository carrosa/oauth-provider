package no.fotogjengen.hilfling.authserver

import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.enums.Role
import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.slf4j.LoggerFactory
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component
import java.util.*

@Component
class Bootstrap(
        private val userRepository: UserRepository,
        private val passwordEncoder: PasswordEncoder
) : ApplicationListener<ApplicationReadyEvent> {

    val LOG = LoggerFactory.getLogger(Bootstrap::class.java)

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        LOG.info("Verifying if default user exists")
        createUserWithRole(
                "carosa",
                "pass",
                "carolinesandsbraten@gmail.com",
                Role.ADMIN
        )
    }

    private fun createUserWithRole(username: String, password: String, email: String, authority: Role) {
        if(userRepository.findByUsername(username) == null) {
            val user = User(
                    username = username,
                    password = passwordEncoder.encode(password),
                    email = email,
                    role = authority,
                    firstName = username,
                    lastName = username,
                    enabled = true
            )
            userRepository.save(user)
        }
    }
}
