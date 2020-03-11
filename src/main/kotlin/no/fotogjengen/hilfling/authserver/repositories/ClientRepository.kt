package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
* Repository contains CRUD operations for Client Model
* */
@Repository // Create a repository Bean
interface ClientRepository : JpaRepository<Client, Long> {
    fun findByClientId(clientId: String): Client? // Create method to find by clientId field in DB
}