package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmailDomain
import org.springframework.data.jpa.repository.JpaRepository

interface EmailDomainWhitelistRepository : JpaRepository<WhitelistedEmailDomain, Long> {
    fun findByDomain(domain: String): WhitelistedEmailDomain?
}