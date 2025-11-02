package creotech.crud.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@JsonIgnoreProperties(ignoreUnknown = true)
public record RegisterUserRequest(
        @NotBlank
        @Email
        String email,

        @NotBlank
        @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
        String name,

        @NotBlank
        @Size(min = 6, max = 100, message = "Password must be at least 6 characters")
        String password
) {}
