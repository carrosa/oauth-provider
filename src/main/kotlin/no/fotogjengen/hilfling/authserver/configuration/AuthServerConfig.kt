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
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer
import org.springframework.security.oauth2.provider.CompositeTokenGranter
import org.springframework.security.oauth2.provider.TokenGranter
import org.springframework.security.oauth2.provider.client.ClientCredentialsTokenGranter
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint
import org.springframework.security.oauth2.provider.implicit.ImplicitTokenGranter
import org.springframework.security.oauth2.provider.password.ResourceOwnerPasswordTokenGranter
import org.springframework.security.oauth2.provider.refresh.RefreshTokenGranter
import org.springframework.security.oauth2.provider.token.DefaultTokenServices
import org.springframework.security.oauth2.provider.token.TokenStore
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore

@Configuration
@EnableAuthorizationServer
class AuthServerConfig(
        private val passwordEncoder: PasswordEncoder,
        private val authenticationManager: AuthenticationManager
) : AuthorizationServerConfigurerAdapter() {

    @Bean
    fun accessTokenConverter(): JwtAccessTokenConverter {
        val converter = JwtAccessTokenConverter()
        converter.setSigningKey("123456")
        return converter
    }

    @Bean
    fun tokenStore(): TokenStore {
        return JwtTokenStore(accessTokenConverter())
    }

    @Bean
    @Primary
    fun tokenServices(): DefaultTokenServices {
        val defaultTokenServices = DefaultTokenServices()
        defaultTokenServices.setTokenStore(tokenStore())
        defaultTokenServices.setSupportRefreshToken(true) // Should maybe be false?? Check for PKCE
        return defaultTokenServices
    }

    override fun configure(endpoints: AuthorizationServerEndpointsConfigurer) {
        endpoints.authorizationCodeServices(
                PkceAuthorizationCodeServices(endpoints.clientDetailsService, passwordEncoder)
        ).tokenGranter(tokenGranter(endpoints))

        endpoints.authenticationManager(authenticationManager)
                .tokenStore(tokenStore())
                .accessTokenConverter(accessTokenConverter())
    }

    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.allowFormAuthenticationForClients()
    }

    private fun tokenGranter(endpoints: AuthorizationServerEndpointsConfigurer): TokenGranter {
        val granters = ArrayList<TokenGranter>()
        val tokenServices = endpoints.tokenServices
        val authorizationCodeServices = endpoints.authorizationCodeServices
        val clientDetailsService = endpoints.clientDetailsService
        val requestFactory = endpoints.oAuth2RequestFactory

        granters.add(RefreshTokenGranter(tokenServices, clientDetailsService, requestFactory))
        granters.add(ImplicitTokenGranter(tokenServices, clientDetailsService, requestFactory))
        granters.add(ClientCredentialsTokenGranter(tokenServices, clientDetailsService, requestFactory))
        granters.add(ResourceOwnerPasswordTokenGranter(
                authenticationManager,
                tokenServices,
                clientDetailsService, requestFactory
        ))
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
        clients.inMemory()
                .withClient("public")
                .secret("{noop}")
                .redirectUris("http://public-client/")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .autoApprove(true)
    }

    /*@Throws(Exception::class)
    override fun configure(clients: ClientDetailsServiceConfigurer) {

    }*/
}