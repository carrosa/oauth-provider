package no.fotogjengen.hilfling.authserver.entities

import javax.persistence.*
import javax.validation.constraints.Email

/*
* Holds whitelisted emails
* */
@Entity // Is a database entity
@Table(name = "whitelisted_email") // Table name in DB is whitelisted_email
data class WhitelistedEmail(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        @Email
        val email: String
)