package no.fotogjengen.hilfling.authserver.configuration

import no.fotogjengen.hilfling.authserver.configuration.pkce.PkceAuthorizationCodeServices
import no.fotogjengen.hilfling.authserver.configuration.pkce.PkceAuthorizationCodeTokenGranter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.TokenGranter
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

/*
* @Configuration markes this as a configuration bean
*
* @Autowired passwordEncoder, which password encoder to use (we use bcrypt)
* @Autowired authenticationManager, holds method for authentication aka this is used to authenticate users
* @Autowired clientDetailsService, holds method to load client when trying to authenticate a user
* */
@Configuration
class AuthServerConfig(
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager,
        private val clientDetailsService: ClientDetailsService
) : AuthorizationServerConfigurerAdapter() {

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        /*
        * Converts access tokens to JWT access tokens
        * */
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey("123456")
        return converter
    }

    @Bean
    fun tokenStore(): TokenStore {
        /*
        * Defines which token store to use
        * */
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        /*
        * Defines which token store to use
        * We use a JWT token store
        * @return DefaultTokenServices, which is the standard token service
        *   when you decide that you need a token service
        * */
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true) // Should maybe be false?? Check for PKCE
        return defaultTokenServices
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        // Add PKCE requirement to authorization code grant
        endpoints.authorizationCodeServices(
                PkceAuthorizationCodeServices(endpoints.clientDetailsService, passwordEncoder)
        ).tokenGranter(tokenGranter(endpoints))

        // Define which token store to use
        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        /*
        * Allow authentication (login)
        * */
        security.allowFormAuthenticationForClients()
    }

    private fun tokenGranter(endpoints: AuthorizationServerEndpointsConfigurer): TokenGranter {
        /*
        * Add methods to grant different tokens
        * @return A composite of multiple access token grant methods
        * */
        val granters = ArrayList<TokenGranter>()
        val tokenServices = endpoints.tokenServices
        val authorizationCodeServices = endpoints.authorizationCodeServices
        val clientDetailsService = endpoints.clientDetailsService
        val requestFactory = endpoints.oAuth2RequestFactory

        // Add refresh token as grant method to get access token
        granters.add(RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory))
        // Add refresh token as grant method to get access token
        granters.add(ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory))
        // Add client credentials as grant method to get access token
        granters.add(ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory))
        // Add resource owner password as grant method to get access token
        granters.add(ResourceOwnerPasswordTokenGranter(
                authenticationManager,
                tokenServices,
                clientDetailsService, requestFactory
        ))
        // Add Authorization code (with PKCE) as grant method to get access token
        granters.add(PkceAuthorizationCodeTokenGranter(
                tokenServices,
                authorizationCodeServices as PkceAuthorizationCodeServices,
                clientDetailsService,
                requestFactory
        ))
        return CompositeTokenGranter(granters)
    }

    @Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {
        /*
        * Define where to get clients from
        * */
        // clients.withClientDetails(clientDetailsService) TODO make this work (for dynamic client registration)
        clients.inMemory()
                .withClient("public")
                .secret("{noop}")
                .redirectUris(
                        "http://localhost/",
                        "http://localhost:9000/",
                        "http://localhost:8000/",
                        "http://localhost:3000/"
                )
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .autoApprove(true)
    }
}