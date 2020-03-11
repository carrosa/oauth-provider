package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.ClientRepository
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Service

@Service
class ClientDetailsServiceImpl(
        private val clientRepository: ClientRepository
) : ClientDetailsService {
    override fun loadClientByClientId(clientId: String): ClientDetails? {
        val loadedClient = clientRepository.findByClientId(clientId)
        if (loadedClient != null) {
            val client = BaseClientDetails()
            client.clientId = clientId
            client.clientSecret = loadedClient.clientSecret
            client.setAuthorizedGrantTypes(loadedClient.authorizedGrantTypes)
            client.setScope(loadedClient.scope)
            client.accessTokenValiditySeconds = loadedClient.accessTokenValiditySeconds
            client.refreshTokenValiditySeconds = loadedClient.refreshTokenValiditySeconds
            client.registeredRedirectUri = loadedClient.registeredRedirectUri
            return client
        }
        return null
    }
}