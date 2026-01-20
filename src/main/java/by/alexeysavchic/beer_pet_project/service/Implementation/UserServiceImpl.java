package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.response.GetUserResponse;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{
    UserRepository repository;

    public UserServiceImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public GetUserResponse findUserById(Long id)
    {
        return null;
    }

    @Override
    public GetUserResponse findUserByUsername(String username)
    {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
