package by.alexeysavchic.beer_pet_project.dto.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.ResponseCookie;

@Getter
@Setter
@NoArgsConstructor
public class CookieDTO
{
    private ResponseCookie baseCookie;
    private ResponseCookie refreshCookie;
}
