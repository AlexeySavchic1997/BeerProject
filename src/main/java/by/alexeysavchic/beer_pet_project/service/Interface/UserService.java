package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.RefreshTokenRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtAuthentificationDTO;
import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetUserResponse;
import org.springframework.http.ResponseCookie;

public interface UserService
{
    public void signUp(UserRegisterRequest request);

    public ResponseCookie logIn(LogInRequest request);

}
