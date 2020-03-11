package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.springframework.security.oauth2.common.exceptions.InvalidGrantException
import org.springframework.security.oauth2.provider.OAuth2Authentication

/*
* Container class
* Holds authentication and associated code challenge and code challenge method
* */
class PkceProtectedAuthentication {
    private val codeChallenge: String?
    private val codeChallengeMethod: CodeChallengeMethod
    private val authentication: OAuth2Authentication

    constructor(authentication: OAuth2Authentication) {
        // Constructor without code challenge
        this.codeChallenge = null
        this.codeChallengeMethod = CodeChallengeMethod.NONE
        this.authentication = authentication
    }

    constructor(codeChallenge: String, codeChallengeMethod: CodeChallengeMethod, authentication: OAuth2Authentication) {
        // Constructor with code challenge
        this.codeChallenge = codeChallenge
        this.codeChallengeMethod = codeChallengeMethod
        this.authentication = authentication
    }

    fun getAuthentication(codeVerifier: String): OAuth2Authentication {
        /*
        * @param codeVerifier used in code challenge
        * @return OAuth2Authentication, authentication details of user who is authenticated with code challenge
        *   the returned object has access to client details, credentials, etc.
        * */
        return when {
            codeChallengeMethod == CodeChallengeMethod.NONE -> {
                authentication
            }
            codeChallengeMethod.transform(codeVerifier) == codeChallenge -> {
                authentication
            }
            else -> {
                throw InvalidGrantException("Invalid code verifier")
            }
        }
    }
}