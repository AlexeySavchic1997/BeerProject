package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtDTO;

public interface AuthService
{
    public JwtDTO signUp(UserRegisterRequest request);

    public JwtDTO logIn(LogInRequest request);

    public String refresh(String token);

}
