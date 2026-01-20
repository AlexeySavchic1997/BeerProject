package by.alexeysavchic.beer_pet_project.service.Interface;

import by.alexeysavchic.beer_pet_project.dto.response.GetUserResponse;

import java.util.List;

public interface UserService
{
    public GetUserResponse findUserById(Long id);

    public GetUserResponse findUserByUsername(String username);

    public void deleteUser(Long id);
}
