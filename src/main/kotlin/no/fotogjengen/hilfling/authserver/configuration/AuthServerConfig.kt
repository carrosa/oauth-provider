package no.fotogjengen.hilfling.authserver.configuration

import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer

@Configuration
@EnableAuthorizationServer
class AuthServerConfig : AuthorizationServerConfigurerAdapter() {
    override fun configure(security: AuthorizationServerSecurityConfigurer) {
        security.allowFormAuthenticationForClients()
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
                .and()
                .withClient("private")
                .secret("{noop}secret")
                .redirectUris("http://private-client/")
                .authorizedGrantTypes("authorization_code")
                .scopes("read")
                .autoApprove(true)
    }
}