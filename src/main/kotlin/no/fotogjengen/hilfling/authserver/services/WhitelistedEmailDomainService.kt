package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.WhitelistedEmailDomainRepository
import org.springframework.stereotype.Service

/*
* @Service Indicates this is a service class, also creates a bean
* @Autowired repository, gives access to WhitelistedEmailDomain methods in this class
* */
@Service
class WhitelistedEmailDomainService(private val whitelistedEmailDomainRepository: WhitelistedEmailDomainRepository) {
    fun isEmailDomainWhitelisted(domain: String): Boolean {
        /*
        * @param domain, String
        * @return Boolean, is the email domain param whitelisted
        * */
        val whitelistedDomain = whitelistedEmailDomainRepository.findByDomain(domain)
        return whitelistedDomain?.domain == domain
    }

    fun getWhitelistedEmailDomains(): List<String> {
        /*
        * @return List<String>, list of all whitelisted email domains
        * */
        return whitelistedEmailDomainRepository.findAll().map { it.domain }
    }
}