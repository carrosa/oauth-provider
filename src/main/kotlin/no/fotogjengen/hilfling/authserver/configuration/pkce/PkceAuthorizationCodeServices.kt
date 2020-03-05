package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.oauth2.common.exceptions.InvalidRequestException
import org.springframework.security.oauth2.common.util.RandomValueStringGenerator
import org.springframework.security.oauth2.provider.ClientDetailsService
import org.springframework.security.oauth2.provider.OAuth2Authentication
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices
import java.util.*
import java.util.concurrent.ConcurrentHashMap

class PkceAuthorizationCodeServices : AuthorizationCodeServices {
    private val generator = RandomValueStringGenerator()
    private val authorizationCodeStore = ConcurrentHashMap<String, PkceProtectedAuthentication>()

    private val clientDetailsService: ClientDetailsService
    private val passwordEncoder: PasswordEncoder

    constructor(clientDetailsService: ClientDetailsService, passwordEncoder: PasswordEncoder) {
        this.clientDetailsService = clientDetailsService
        this.passwordEncoder = passwordEncoder
    }

    override fun createAuthorizationCode(authentication: OAuth2Authentication): String {
        val protectedAuthentication = getProtectedAuthentication(authentication)
        val code = generator.generate()
        authorizationCodeStore[code] = protectedAuthentication
        return code
    }

    override fun consumeAuthorizationCode(code: String): OAuth2Authentication {
        throw UnsupportedOperationException()
    }

    private fun getProtectedAuthentication(authentication: OAuth2Authentication): PkceProtectedAuthentication {
        val requestParams = authentication.oAuth2Request.requestParameters
        if (isPublicClient(requestParams["client_id"]) && !requestParams.containsKey("code_challenge")) {
            throw InvalidRequestException("Code Challenge Required.")
        }
        if (requestParams.containsKey("code_challenge")) {
            val codeChallenge = requestParams["code_challenge"].toString()
            val codeChallengeMethod = getCodeChallengeMethod(requestParams)
            return PkceProtectedAuthentication(codeChallenge, codeChallengeMethod, authentication)
        }
        return PkceProtectedAuthentication(authentication)
    }

    private fun getCodeChallengeMethod(requestParams: Map<String, String>): CodeChallengeMethod {
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
        val clientSecret = clientDetailsService.loadClientByClientId(clientId).clientSecret
        return clientSecret == null || passwordEncoder.matches("", clientSecret) // null or empty
    }

    fun consumeAuthorizationCodeAndCodeVerifier(code: String, verifier: String): OAuth2Authentication? {
        return authorizationCodeStore[code]?.getAuthentication(verifier)
    }
}