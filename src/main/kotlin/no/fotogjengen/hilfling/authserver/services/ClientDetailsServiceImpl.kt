package no.fotogjengen.hilfling.authserver.services

import no.fotogjengen.hilfling.authserver.repositories.ClientRepository
import org.springframework.security.oauth2.provider.ClientDetails
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.client.BaseClientDetails
import org.springframework.stereotype.Service

/*
* @Service Indicates this is a service class, also creates a bean
* @Autowired clientRepository, gives access to ClientRepository methods in this class
* */
@Service
class ClientDetailsServiceImpl(
        private val clientRepository: ClientRepository
) : ClientDetailsService {
    override fun loadClientByClientId(clientId: String): ClientDetails? {
        /*
        * @param clientId, Id of oauth2 client that tries to access the application
        * @return ClientDetails or null if no client with clientId exists
        * */
        val loadedClient = clientRepository.findByClientId(clientId)
        if (loadedClient != null) { // if no client in database with clientId, return null
            /*
            * Sets necessary Client Details in correct format, based on values found in database
            * */
            val client = BaseClientDetails()
            client.clientId = clientId
            client.clientSecret = loadedClient.clientSecret
            client.setAuthorizedGrantTypes(loadedClient.authorizedGrantTypes)
            client.setScope(loadedClient.scope)
            client.accessTokenValiditySeconds = loadedClient.accessTokenValiditySeconds
            client.refreshTokenValiditySeconds = loadedClient.refreshTokenValiditySeconds
            client.registeredRedirectUri = loadedClient.registeredRedirectUri.toSet()
            return client
        }
        return null
    }
}