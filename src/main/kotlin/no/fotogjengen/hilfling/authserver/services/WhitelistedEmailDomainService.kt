package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.EmailDomainWhitelistRepository
import org.springframework.stereotype.Service

@Service
class WhitelistedEmailDomainService(private val emailDomainWhitelistRepository: EmailDomainWhitelistRepository) {
    fun isEmailDomainWhitelisted(domain: String): Boolean {
        val whitelistedDomain = emailDomainWhitelistRepository.findByDomain(domain)
        return whitelistedDomain?.domain == domain
    }

    fun getWhitelistedEmailDomains(): List<String> {
        return emailDomainWhitelistRepository.findAll().map { it.domain }
    }
}