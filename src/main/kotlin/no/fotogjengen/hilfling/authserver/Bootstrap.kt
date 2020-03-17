package no.fotogjengen.hilfling.authserver

import no.fotogjengen.hilfling.authserver.entities.Client
import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmailDomain
import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmail
import no.fotogjengen.hilfling.authserver.enums.Role
import no.fotogjengen.hilfling.authserver.repositories.ClientRepository
import no.fotogjengen.hilfling.authserver.repositories.WhitelistedEmailDomainRepository
import no.fotogjengen.hilfling.authserver.repositories.WhitelistedEmailRepository
import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

/*
* Bootstrap application with default values in database
* Populates using respective repositories
*
* @Autowired userRepository, CRUD repository for users
* @Autowired whitelistedEmailDomainRepository, CRUD methods for whitelisted email domains
* @Autowired whitelistedEmailRepository, CRUD methods for whitelisted emails
* @Autowired clientRepository, CRUD methods for clients
* @Autowired passwordEncoder, default password encoder (bcrypt)
* */
@Component
class Bootstrap(
        private val userRepository: UserRepository,
        private val whitelistedEmailDomainRepository: WhitelistedEmailDomainRepository,
        private val whitelistedEmailRepository: WhitelistedEmailRepository,
        private val clientRepository: ClientRepository,
        private val passwordEncoder: PasswordEncoder
) : ApplicationListener<ApplicationReadyEvent> {

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
        /*
        * @param username
        * @param password, plaintext
        * @param email, must follow @ValidEmail constraint
        * @param authority, which role should the user have (access permission)
        *
        * Creates a user and saves to database
        * */
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
        /*
        * @param domain, email domain to be whitelisted
        *
        * Creates a whitelisted email domain and saves it to the database
        * */
        if (whitelistedEmailDomainRepository.findByDomain(domain) == null) {
            val whitelistedDomain = WhitelistedEmailDomain(domain = domain)
            whitelistedEmailDomainRepository.save(whitelistedDomain)
        }
    }

    private fun createWhitelistedEmail(email: String) {
        /*
        * @param email, email to be whitelisted
        *
        * Creates a whitelisted email and saves it to the database
        * */
        if (whitelistedEmailRepository.findByEmail(email) == null) {
            val whitelistedEmail = WhitelistedEmail(email = email)
            whitelistedEmailRepository.save(whitelistedEmail)
        }
    }

    private fun createClient(clientId: String) {
        /*
        * @param clientId, id of the client (only field that really has to be unique)
        * TODO: more configuration options here
        *
        * Creates a client and saves it to the database
        * */
        if (clientRepository.findByClientId(clientId) == null) {
            val client = Client(
                    clientSecret = "{noop}",
                    scope = arrayListOf("read"),
                    registeredRedirectUri = arrayListOf("http://public-client/", "localhost:8080"),
                    clientId = clientId,
                    authorizedGrantTypes = arrayListOf(
                            "authorization_code"
                    ) // default authorization code
            )
            clientRepository.save(client)
        }
    }
}
