package no.fotogjengen.hilfling.authserver

import no.fotogjengen.hilfling.authserver.entities.Client
import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmailDomain
import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmail
import no.fotogjengen.hilfling.authserver.enums.Role
import no.fotogjengen.hilfling.authserver.repositories.ClientRepository
import no.fotogjengen.hilfling.authserver.repositories.EmailDomainWhitelistRepository
import no.fotogjengen.hilfling.authserver.repositories.EmailWhitelistRepository
import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class Bootstrap(
        private val userRepository: UserRepository,
        private val domainWhitelistRepository: EmailDomainWhitelistRepository,
        private val emailWhitelistRepository: EmailWhitelistRepository,
        private val clientRepository: ClientRepository,
        private val passwordEncoder: PasswordEncoder
) : ApplicationListener<ApplicationReadyEvent> {

    /*
    * Bootstrap application with default values in database
    * Populates using respective repositories
    * */

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        createUserWithRole(
                "carosa",
                "pass",
                "carolinesandsbraten@gmail.com",
                Role.ADMIN
        )
        createWhitelistedEmailDomain("samfundet.no")
        createWhitelistedEmail("carolinesandsbraten@gmail.com")
        createClient("public")
    }

    private fun createUserWithRole(username: String, password: String, email: String, authority: Role) {
        if (userRepository.findByUsername(username) == null) {
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

    private fun createWhitelistedEmailDomain(domain: String) {
        if (domainWhitelistRepository.findByDomain(domain) == null) {
            val whitelistedDomain = WhitelistedEmailDomain(domain = domain)
            domainWhitelistRepository.save(whitelistedDomain)
        }
    }

    private fun createWhitelistedEmail(email: String) {
        if (emailWhitelistRepository.findByEmail(email) == null) {
            val whitelistedEmail = WhitelistedEmail(email = email)
            emailWhitelistRepository.save(whitelistedEmail)
        }
    }

    private fun createClient(clientId: String) {
        if (clientRepository.findByClientId(clientId) == null) {
            val client = Client(
                    clientSecret = "{noop}",
                    scope = mutableSetOf("read"),
                    registeredRedirectUri = mutableSetOf("http://public-client/", "localhost:8080"),
                    clientId = clientId
            )
            clientRepository.save(client)
        }
    }
}
