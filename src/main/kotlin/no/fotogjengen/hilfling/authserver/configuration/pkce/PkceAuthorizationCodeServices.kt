package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
import java.util.*
import java.util.concurrent.ConcurrentHashMap

/*
* Class handles authorization code creation and consumption (exchange for access token)
*
* @Autowired clientDetailsService, holds method to load client when trying to authenticate a user
* @Autowired passwordEncoder, which password encoder to use (we use bcrypt)
* */
class PkceAuthorizationCodeServices(
        private val clientDetailsService: ClientDetailsService,
        private val passwordEncoder: PasswordEncoder
) : AuthorizationCodeServices {
    private val generator = RandomValueStringGenerator()
    private val authorizationCodeStore = ConcurrentHashMap<String, PkceProtectedAuthentication>()

    override fun createAuthorizationCode(authentication: OAuth2Authentication): String {
        /*
        * @param authentication, OAuth2Authentication object, used to get authorization code for Oauth2
        * @return authorization code
        * */
        val protectedAuthentication = getProtectedAuthentication(authentication)
        val code = generator.generate()
        authorizationCodeStore[code] = protectedAuthentication
        return code
    }

    override fun consumeAuthorizationCode(code: String): OAuth2Authentication {
        /*
        * No code challenge
        * */
        throw UnsupportedOperationException()
    }

    private fun getProtectedAuthentication(authentication: OAuth2Authentication): PkceProtectedAuthentication {
        /*
        * @param authentication, OAuth2Authentication object, holds OAuth2 details
        * @return PkceProtectedAuthentication object which holds authorization information
        *   such as code challenge etc. (see class for more info)
        * */
        val requestParams = authentication.oAuth2Request.requestParameters
        if (isPublicClient(requestParams["client_id"]) && !requestParams.containsKey("code_challenge")) {
            // request doesn't contain enough info to do perform a code challenge, so throw error
            throw InvalidRequestException("Code Challenge Required.")
        }
        if (requestParams.containsKey("code_challenge")) {
            // Do code challenge
            val codeChallenge = requestParams["code_challenge"].toString()
            val codeChallengeMethod = getCodeChallengeMethod(requestParams)
            return PkceProtectedAuthentication(codeChallenge, codeChallengeMethod, authentication)
        }
        // Use without PKCE, only Authorization Code grant
        return PkceProtectedAuthentication(authentication)
    }

    private fun getCodeChallengeMethod(requestParams: Map<String, String>): CodeChallengeMethod {
        /*
        * @param requestParams,
        * @return CodeChallengeMethod, enum which decides which code challenge method to use.
        *   Either SHA-256, PLAIN or NONE (can of course be extended to more methods if wanted)
        * */
        try {
            return Optional.ofNullable(requestParams["code_challenge_method"])
                    .map(String::toUpperCase)
                    .map(CodeChallengeMethod::valueOf)
                    .orElse(CodeChallengeMethod.PLAIN)
        } catch (e: IllegalArgumentException) {
            throw InvalidRequestException("Transform algorithm not supported")
        }
    }

    private fun isPublicClient(clientId: String?): Boolean {
        /*
        * @param clientId
        * @return Boolean, true if the client is public (no client secret), false if this is not the case
        * */
        val clientSecret = clientDetailsService.loadClientByClientId(clientId).clientSecret
        return clientSecret == null || passwordEncoder.matches("", clientSecret) // null or empty
    }

    fun consumeAuthorizationCodeAndCodeVerifier(code: String, verifier: String): OAuth2Authentication? {
        /*
        * @param code, Authorization Code (OAuth2 standard)
        * @param verifier, code verifier (OAuth2 standard, PKCE)
        * @return Authentication object containing OAuth2 data
        * */
        return authorizationCodeStore[code]?.getAuthentication(verifier)
    }
}