package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtResponseDTO;
import jakarta.servlet.http.HttpServletRequest;

public interface AuthService {
    public JwtResponseDTO signUp(UserRegisterRequest request);

    public JwtResponseDTO logIn(LogInRequest request);

    public JwtResponseDTO refresh(HttpServletRequest request);
}
