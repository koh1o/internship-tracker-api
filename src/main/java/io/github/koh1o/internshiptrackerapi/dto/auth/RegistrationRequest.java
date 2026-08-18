package io.github.koh1o.internshiptrackerapi.dto.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record RegistrationRequest(

        @NotBlank(message = "Email required")
        @Email(message = "Email must be valid")
        String email,

        @NotBlank(message = "Password required")
        String password
) {
}
