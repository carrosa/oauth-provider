package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ClientRepository : JpaRepository<Client, Long> {
    fun findByClientId(clientId: String): Client?
}