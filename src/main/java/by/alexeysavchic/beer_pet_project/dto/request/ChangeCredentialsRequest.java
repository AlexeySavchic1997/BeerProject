package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangeCredentialsRequest {
    @Size(min = 2, max = 20, message = "username must be between 2 and 20 symbols")
    @NotBlank
    private String username;

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "password must be between 8 and 15 symbols")
    private String oldPassword;

    @NotBlank
    @Size(min = 8, max = 15, message = "password must be between 8 and 15 symbols")
    private String newPassword;
}
