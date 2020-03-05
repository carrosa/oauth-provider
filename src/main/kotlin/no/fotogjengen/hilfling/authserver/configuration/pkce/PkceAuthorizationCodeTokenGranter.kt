package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.springframework.security.oauth2.common.exceptions.InvalidClientException
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.exceptions.RedirectMismatchException
import org.springframework.security.oauth2.provider.*
import org.springframework.security.oauth2.provider.code.AuthorizationCodeTokenGranter
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices

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
        val parameters = tokenRequest.requestParameters
        val authorizationCode = parameters["code"]
        val redirectUri = parameters["redirect_uri"]
        if (authorizationCode == null) {
            throw InvalidRequestException("An authorization code must be supplied.")
        } else {
            val codeVerifier = parameters.getOrDefault("code_verifier", "")
            val storedAuth = authorizationCodeServices.consumeAuthorizationCodeAndCodeVerifier(
                    authorizationCode,
                    codeVerifier
            )
            if (storedAuth == null) {
                throw InvalidGrantException("Invalid authorization code: $authorizationCode")
            } else {
                val pendingOauth2Request = storedAuth.oAuth2Request
                val redirectUriApprovalParameter = pendingOauth2Request.requestParameters["redirect_uri"]
                if (
                        (redirectUri != null || redirectUriApprovalParameter != null) &&
                        pendingOauth2Request.redirectUri != redirectUri
                ) {
                    throw RedirectMismatchException("Redirect URI mismatch.")
                } else {
                    val pendingClientId = pendingOauth2Request.clientId
                    val clientId = tokenRequest.clientId
                    if (clientId != null && clientId != pendingClientId) {
                        throw InvalidClientException("Client ID mismatch.")
                    } else {
                        val combinedParameters = HashMap<String, String>(pendingOauth2Request.requestParameters)
                        combinedParameters.putAll(parameters)
                        val finalStoredOauth2Request = pendingOauth2Request.createOAuth2Request(combinedParameters)
                        val userAuth = storedAuth.userAuthentication
                        return OAuth2Authentication(finalStoredOauth2Request, userAuth)
                    }
                }
            }

        }
    }
}