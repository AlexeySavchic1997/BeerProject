package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.CookieDTO;
import org.springframework.http.ResponseCookie;

public interface AuthService
{
    public CookieDTO signUp(UserRegisterRequest request);

    public CookieDTO logIn(LogInRequest request);

    public ResponseCookie refresh(String token);

}
