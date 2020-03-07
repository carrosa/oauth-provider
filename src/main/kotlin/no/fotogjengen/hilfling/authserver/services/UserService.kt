package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.stereotype.Service

@Service
class UserService(private val repository: UserRepository) {
    fun registerNewUserAccount(user: User): User {
        // TODO: check if user exists
        return repository.save(user)
    }

    fun isUsernameAlreadyInUse(username: String): Boolean {
        val user = repository.findByUsername(username)
        return user?.username == username
    }

    fun isEmailAlreadyInUse(email: String): Boolean {
        val user = repository.findByEmail(email)
        return user?.email == email
    }
}