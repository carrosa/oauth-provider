package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.UserRepository
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service("customUserDetailsService")
class UserDetailsServiceImpl(
        private val userRepository: UserRepository
) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByUsername(username)
        val grantedAuthorities = HashSet<GrantedAuthority>()
        grantedAuthorities.add(SimpleGrantedAuthority(user.role.toString()))
        return User(user.username, user.password, grantedAuthorities)
    }
}