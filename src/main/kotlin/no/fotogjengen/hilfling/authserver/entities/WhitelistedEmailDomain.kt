package no.fotogjengen.hilfling.authserver.entities

import javax.persistence.*

/*
* Holds whitelisted domains
* */
@Entity // Is a database enitity
@Table(name = "whitelisted_domain") // Table name in DB is whitelisted_domain
data class WhitelistedEmailDomain (
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        val domain: String
)