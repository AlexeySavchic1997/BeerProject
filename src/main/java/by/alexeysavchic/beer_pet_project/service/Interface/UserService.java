package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.JwtAuthentificationDTO;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetUserResponse;

import java.util.List;

public interface UserService
{
    public JwtAuthentificationDTO signUp(UserRegisterRequest request);

    public GetUserResponse findUserById(Long id);

    public GetUserResponse findUserByUsername(String username);

    public GetUserResponse findUserByEmail(String email);

    public void deleteUser(Long id);
}
