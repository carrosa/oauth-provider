package no.fotogjengen.hilfling.authserver.entities

import javax.persistence.*


/*
* Database entity class to hold oauth client information
* */
@Entity // Is a database entity
@Table(name = "auth_client") // Table name in DB is auth_client
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
        val registeredRedirectUri: ArrayList<String>,

        @Column(nullable = false)
        val scope: ArrayList<String>,

        @Column(nullable = false)
        val authorizedGrantTypes: ArrayList<String>

)
