package by.alexeysavchic.beer_pet_project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class JwtDTO
{
    private String baseToken;
    private String refreshToken;
}
