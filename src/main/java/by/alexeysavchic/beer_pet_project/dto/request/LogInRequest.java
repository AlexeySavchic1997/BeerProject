package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class LogInRequest
{
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String password;
}
