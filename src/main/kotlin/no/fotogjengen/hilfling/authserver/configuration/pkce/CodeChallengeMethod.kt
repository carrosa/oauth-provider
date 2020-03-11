package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.bouncycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


/*
* Enum with different Code Challenge methods
* @enum S256, use SHA-256 hash to solve code challenge
* @enum PLAIN, code challenge is not hashed
* @enum NONE, unsupported operation, translates to not using PKCE at all
* */
enum class CodeChallengeMethod {
    S256 {
        override fun transform(codeVerifier: String): String {
            /*
            * @param codeVerifier, codeVerifier to hash
            * @return code challenge
            * */
            try {
                val digest = MessageDigest.getInstance("SHA-256")
                val hash = digest.digest(codeVerifier.toByteArray(StandardCharsets.UTF_8))
                return Base64.getUrlEncoder().encodeToString(Hex.encode(hash))
            } catch (e: NoSuchAlgorithmException) {
                throw IllegalStateException(e)
            }
        }
    },
    PLAIN {
        override fun transform(codeVerifier: String): String {
            /*
            * @param codeVerifier
            * @return codeChallenge
            * In PLAIN code challenge the verifier and challenge is the same, aka no cryptography :((
            * Bad bad boy if a client ever uses this.
            * */
            return codeVerifier
        }
    },
    NONE {
        override fun transform(codeVerifier: String): String {
            /*
            * Just don't, this is a todo
            * but we shouldn't use this at all anyways
            * unless we start using server side applications, then this have to be implemented
            * */
            throw UnsupportedOperationException()
        }
    };

    abstract fun transform(codeVerifier: String): String // implemented in enum
}