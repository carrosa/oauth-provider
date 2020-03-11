package no.fotogjengen.hilfling.authserver.repositories

import no.fotogjengen.hilfling.authserver.entities.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

/*
* Repository contains CRUD operations for User Model
* */
@Repository // Create a repository Bean
interface UserRepository : JpaRepository<User, Long> {
    fun findByUsername(username: String): User? // Create method to find user by username
    fun findByEmail(email: String): User? // Create method to find user by email
}