package no.fotogjengen.hilfling.authserver.entities

import no.fotogjengen.hilfling.authserver.enums.Role
import org.hibernate.annotations.CreationTimestamp
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email

@Entity
@Table(name = "auth_user")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long = 0,

        @Column(unique = true, nullable = false)
        val username: String,

        @Column(nullable = false)
        val password: String,

        @Column(nullable = false)
        val firstName: String,

        @Column(nullable = false)
        val lastName: String,

        @Column(nullable = false)
        @Email(message = "Please provide a valid email." /* TODO: Add regex to only accept @samfundet.no emails */)
        val email: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        val role: Role,

        @Column(nullable = false)
        val enabled: Boolean,

        @Column
        @CreationTimestamp
        val dateCreated: Date = Calendar.getInstance().time
)