package no.fotogjengen.hilfling.authserver.configuration.pkce

import org.bouncycastle.util.encoders.Hex
import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.util.*


enum class CodeChallengeMethod {
    S256 {
        override fun transform(codeVerifier: String): String {
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
            return codeVerifier
        }
    },
    NONE {
        override fun transform(codeVerifier: String): String {
            throw UnsupportedOperationException()
        }
    };

    abstract fun transform(codeVerifier: String): String
}