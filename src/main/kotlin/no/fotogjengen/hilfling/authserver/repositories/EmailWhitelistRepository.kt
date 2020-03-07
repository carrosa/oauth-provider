package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmail
import org.springframework.data.jpa.repository.JpaRepository

interface EmailWhitelistRepository : JpaRepository<WhitelistedEmail, Long> {
    fun findByEmail(email: String): WhitelistedEmail?
}