package no.fotogjengen.hilfling.authserver.entities

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import javax.persistence.*

@Entity
@Table(name = "auth_client")
data class Client(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(nullable = false)
        val accessTokenValiditySeconds: Int = -1, // Valid forever default

        @Column(nullable = false)
        val clientId: String,

        @Column(nullable = false)
        val refreshTokenValiditySeconds: Int = -1, // Valid forever default

        @Column(nullable = false)
        val clientSecret: String,

        @Column(nullable = false)
        val registeredRedirectUri: MutableSet<String>,

        @Column(nullable = false)
        val scope: MutableSet<String>,

        @Column(nullable = false)
        val authorizedGrantTypes: MutableSet<String> = mutableSetOf(
                "authorization_code"
        ) // default authorization code

)
