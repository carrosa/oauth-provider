package no.fotogjengen.hilfling.authserver.entities

import no.fotogjengen.hilfling.authserver.annotations.ValidEmail
import no.fotogjengen.hilfling.authserver.dtos.UserDTO
import no.fotogjengen.hilfling.authserver.enums.Role
import org.hibernate.annotations.CreationTimestamp
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.*
import javax.persistence.*
import javax.validation.constraints.Email


/*
* User database entity
* */
@Entity // Is a database entity
@Table(name = "auth_user") // Table name in DB is auth_user
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
        @Email
        val email: String,

        @Column(nullable = false)
        @Enumerated(EnumType.STRING)
        val role: Role,

        @Column(nullable = false)
        val enabled: Boolean,

        @Column
        @CreationTimestamp
        val dateCreated: Date = Calendar.getInstance().time
) {

        constructor(userDTO: UserDTO, role: Role, passwordEncoder: PasswordEncoder) : this(
                username = userDTO.username,
                password = passwordEncoder.encode(userDTO.password),
                firstName = userDTO.firstName,
                lastName = userDTO.lastName,
                email = userDTO.email,
                role = role,
                enabled = false
        )
}