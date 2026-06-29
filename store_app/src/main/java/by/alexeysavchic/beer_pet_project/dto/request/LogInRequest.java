package by.alexeysavchic.beer_pet_project.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogInRequest {

    @Email(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$",
            message = "Wrong email pattern")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 15, message = "password must be between 8 and 15 symbols")
    private String password;
}
