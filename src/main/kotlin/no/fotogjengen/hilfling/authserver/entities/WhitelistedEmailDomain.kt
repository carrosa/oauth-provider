package no.fotogjengen.hilfling.authserver.entities

import javax.persistence.*

@Entity
@Table(name = "whitelisted_domain")
data class WhitelistedEmailDomain (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        val domain: String
)