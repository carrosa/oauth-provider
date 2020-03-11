package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.springframework.security.oauth2.common.exceptions.InvalidClientException
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException
import org.springframework.security.oauth2.provider.*
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices

/*
* Token granter which uses functionality found in the other classes in the pkce package to perform a code challenge and
*   return a OPAuth2Authentication object if everything is as it should be.
*
* @param tokenServices, contains methods to generate access tokens
* @param authorizationCodeServices, handles authorization code logic
* @param clientDetailsService, handles client logic (client in our case is f.ex. our photo api
* @param requestFactory, contains methods to handle oauth2 requests, f.eks. creates request to create a token
*
* The params are set in the default constructor
* */
class PkceAuthorizationCodeTokenGranter(
        tokenServices: AuthorizationServerTokenServices,
        private val authorizationCodeServices: PkceAuthorizationCodeServices,
        clientDetailsService: ClientDetailsService,
        requestFactory: OAuth2RequestFactory
) : AuthorizationCodeTokenGranter(
        tokenServices,
        authorizationCodeServices,
        clientDetailsService,
        requestFactory
) {

    override fun getOAuth2Authentication(client: ClientDetails, tokenRequest: TokenRequest): OAuth2Authentication {
        /*
        * Verifies authorization code and performs code challenge (using the params in this class)
        *
        * @param client, an oauth2 client
        * @param tokenRequest, a token request, used to ask for a token
        * @return OAuth2Authentication object which is authenticated, else it will throw some errors on the way
        * */
        val parameters = tokenRequest.requestParameters
        val authorizationCode = parameters["code"]
        val redirectUri = parameters["redirect_uri"] // which page the user is to be redirected to after authentication
        if (authorizationCode == null) {
            // No authorization code = we can't give you anything. It is required because of authorization code grant
            throw InvalidRequestException("An authorization code must be supplied.")
        } else {
            val codeVerifier = parameters.getOrDefault("code_verifier", "")
            // Get OAuth2Authentication object
            val storedAuth = authorizationCodeServices.consumeAuthorizationCodeAndCodeVerifier(
                    authorizationCode,
                    codeVerifier
            )
            if (storedAuth == null) {
                // If no authentication object, then the authorization code supplied must be invalid
                throw InvalidGrantException("Invalid authorization code: $authorizationCode")
            } else {
                val pendingOauth2Request = storedAuth.oAuth2Request
                val redirectUriApprovalParameter = pendingOauth2Request.requestParameters["redirect_uri"]
                if (
                        (redirectUri != null || redirectUriApprovalParameter != null) &&
                        pendingOauth2Request.redirectUri != redirectUri
                ) {
                    /*
                    * If redirect is not registered on the client from the database, then throw error because we
                    *   potentially got a bad request here (maybe hacker??? or just bad config in a client)
                    * */
                    throw RedirectMismatchException("Redirect URI mismatch.")
                } else {
                    val pendingClientId = pendingOauth2Request.clientId
                    val clientId = tokenRequest.clientId
                    if (clientId != null && clientId != pendingClientId) {
                        // Confirm client ID is as expected, if not something is of course wrong, throw error
                        throw InvalidClientException("Client ID mismatch.")
                    } else {
                        // Create a OAuth2 request with all the required/given arguments/parameters
                        val combinedParameters = HashMap<String, String>(pendingOauth2Request.requestParameters)
                        combinedParameters.putAll(parameters)
                        val finalStoredOauth2Request = pendingOauth2Request.createOAuth2Request(combinedParameters)
                        val userAuth = storedAuth.userAuthentication
                        // return final authenticated Authentication object
                        return OAuth2Authentication(finalStoredOauth2Request, userAuth)
                    }
                }
            }

        }
    }
}