package no.fotogjengen.hilfling.authserver.dtos


import no.fotogjengen.hilfling.authserver.annotations.ValidEmail
import no.fotogjengen.hilfling.authserver.annotations.ValidName
import no.fotogjengen.hilfling.authserver.annotations.ValidPassword
import no.fotogjengen.hilfling.authserver.annotations.ValidUsername
import javax.validation.constraints.NotNull


data class UserDTO(
        @NotNull
        @ValidUsername
        val username: String,

        @NotNull
        @ValidPassword
        val password: String,

        @NotNull
        @ValidName
        val firstName: String,

        @NotNull
        @ValidName
        val lastName: String,

        @NotNull
        @ValidEmail
        val email: String
) {
    constructor() : this(
            username = "",
            password = "",
            firstName = "",
            lastName = "",
            email = ""
    )
}