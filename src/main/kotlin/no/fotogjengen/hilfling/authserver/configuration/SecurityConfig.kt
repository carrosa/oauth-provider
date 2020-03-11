package no.fotogjengen.hilfling.authserver.configuration

import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.factory.PasswordEncoderFactories
import org.springframework.security.crypto.password.PasswordEncoder


/*
* Security configuration
* Defines everything to do with access control
* */
@Configuration
class SecurityConfig(
        @Qualifier("customUserDetailsService")
        private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        /*
        * Return a password encoder (using bcrypt)
        * */
        return PasswordEncoderFactories.createDelegatingPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Throws(Exception::class)
    override fun configure(http: HttpSecurity) {
        /*
        * Define which urls need to be authenticated and which ones are open to the public
        * */
        http.requestMatchers().antMatchers("/login", "/oauth/authorize")
                .and()
                .authorizeRequests().mvcMatchers("/.well-known/jwks.json").permitAll()
                .and()
                .authorizeRequests().anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
    }

    @Throws(Exception::class)
    override fun configure(auth: AuthenticationManagerBuilder) {
        /*
        * Defines where to find users when authenticating (basically userDB)
        * */
        auth.userDetailsService(userDetailsService)
    }
}