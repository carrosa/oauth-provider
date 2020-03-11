package no.fotogjengen.hilfling.authserver.endpoints

import com.nimbusds.jose.jwk.JWKSet
import com.nimbusds.jose.jwk.RSAKey
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ResponseBody
import java.security.KeyPair

import java.security.interfaces.RSAPublicKey


/*
* Sets up endpoint for clients to find JWKs used to verify their received access tokens
* */
@FrameworkEndpoint // Endpoint is part of the framework (oauth2)
class JwkSetEndpoint {
    val keyPair: KeyPair

    constructor(keyPair: KeyPair) {
        this.keyPair = keyPair
    }

    @ResponseBody
    @GetMapping("/.well-known/jwks.json")
    fun getKey(): MutableMap<String, Any>? {
        val publicKey: RSAPublicKey = keyPair.public as RSAPublicKey
        val key: RSAKey = RSAKey.Builder(publicKey).build()
        return JWKSet(key).toJSONObject()
    }
}