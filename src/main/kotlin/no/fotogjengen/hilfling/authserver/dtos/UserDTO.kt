package no.fotogjengen.hilfling.authserver.dtos


import no.fotogjengen.hilfling.authserver.annotations.ValidEmail
import no.fotogjengen.hilfling.authserver.annotations.ValidName
import no.fotogjengen.hilfling.authserver.annotations.ValidPassword
import no.fotogjengen.hilfling.authserver.annotations.ValidUsername
import javax.validation.constraints.NotNull


/*
* Data class used in /users/register endpoint to pass data
* This is what is required by the user to fill in when registering
* */
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
    // Empty constructor, needed for thymeleaf form initialization
    constructor() : this(
            username = "",
            password = "",
            firstName = "",
            lastName = "",
            email = ""
    )
}