package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmailDomain
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
* Repository contains CRUD operations for WhitelistedEmailDomain Model
* */
@Repository // Create a repository Bean
interface WhitelistedEmailDomainRepository : JpaRepository<WhitelistedEmailDomain, Long> {
    // Create method to find whitelisted email domain by domain
    fun findByDomain(domain: String): WhitelistedEmailDomain?
}