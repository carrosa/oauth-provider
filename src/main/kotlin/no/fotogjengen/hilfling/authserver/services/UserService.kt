package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.entities.User
import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.stereotype.Service

/*
* @Service Indicates this is a service class, also creates a bean
* @Autowired repository, gives access to UserRepository methods in this class
* */
@Service
class UserService(private val repository: UserRepository) {
    fun registerNewUserAccount(user: User): User {
        /*
        * @param user, User object to save to database
        * @return stored User object
        * */
        // TODO: check if user exists
        return repository.save(user)
    }

    fun isUsernameAlreadyInUse(username: String): Boolean {
        /*
        * @param username, String or null
        * @return Boolean, is the username already in use (true) or not (false)
        * */
        val user = repository.findByUsername(username)
        return user?.username == username
    }

    fun isEmailAlreadyInUse(email: String): Boolean {
        /*
        * @param email, String or null
        * @return Boolean, is the email already in use (true) or not (false)
        * */
        val user = repository.findByEmail(email)
        return user?.email == email
    }
}