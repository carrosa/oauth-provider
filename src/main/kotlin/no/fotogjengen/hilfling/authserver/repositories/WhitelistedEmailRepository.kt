package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.WhitelistedEmail
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
* Repository contains CRUD operations for WhitelistedEmail Model
* */
@Repository // Create a repository Bean
interface WhitelistedEmailRepository : JpaRepository<WhitelistedEmail, Long> {
    fun findByEmail(email: String): WhitelistedEmail? // Create method to find whitelisted email by email address
}