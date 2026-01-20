package by.alexeysavchic.beer_pet_project.dto;

import lombok.Data;

@Data
public class JwtAuthentificationDTO
{
    private String token;
    private String refreshToken;
}
