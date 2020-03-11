package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

/*
* @Service indicates this is a service class and creates a bean with name "customUserDetailsService",
*   the bean has to have a name, because there is already created a default userDetailsService bean by spring boot,
*   and we have to be able to differentiate between that and our custom one.
* @Autowired userRepository, gives access to UserRepository CRUD operations
* */
@Service("customUserDetailsService")
class UserDetailsServiceImpl(
        private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        /*
        * @param username, username to be authenticated
        * @return UserDetails, empty UserDetails if no user with username exist
        * */
        val user = userRepository.findByUsername(username)
        val grantedAuthorities = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(user?.role.toString()))
        return User(user?.username, user?.password, grantedAuthorities)
    }
}