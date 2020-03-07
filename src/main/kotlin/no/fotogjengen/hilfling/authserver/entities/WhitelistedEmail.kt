package no.fotogjengen.hilfling.authserver.entities

import javax.persistence.*
import javax.validation.constraints.Email


@Entity
@Table(name = "whitelisted_email")
data class WhitelistedEmail(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        @Email
        val email: String
)