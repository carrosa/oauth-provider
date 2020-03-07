package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.EmailWhitelistRepository
import org.springframework.stereotype.Service

@Service
class WhitelistedEmailService(private val emailWhitelistRepository: EmailWhitelistRepository) {
    fun isEmailWhitelisted(email: String): Boolean {
        val whitelisted = emailWhitelistRepository.findByEmail(email)
        return whitelisted?.email == email
    }
}