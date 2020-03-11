package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.WhitelistedEmailRepository
import org.springframework.stereotype.Service

/*
* @Service Indicates this is a service class, also creates a bean
* @Autowired repository, gives access to WhitelistedEmail methods in this class
* */
@Service
class WhitelistedEmailService(private val whitelistedEmailRepository: WhitelistedEmailRepository) {
    fun isEmailWhitelisted(email: String): Boolean {
        /*
        * @param email, String
        * @return Boolean, true if email is in whitelistedEmail DB table, false if not
        * */
        val whitelisted = whitelistedEmailRepository.findByEmail(email)
        return whitelisted?.email == email
    }
}