package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;

public interface UserService
{
    public void signUp(UserRegisterRequest request);

    public String logIn(LogInRequest request);

}
