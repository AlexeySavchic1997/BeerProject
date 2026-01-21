package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.JwtAuthentificationDTO;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.GetUserResponse;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{

    UserRepository repository;

    JwtService jwtService;

    UserMapper userMapper;

    public UserServiceImpl(UserRepository repository, JwtService jwtService, UserMapper userMapper) {
        this.repository = repository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
    }

    @Override
    public JwtAuthentificationDTO signUp(UserRegisterRequest request)
    {
        User user = userMapper.userRegisterRequestToUser(request);
        repository.save(user);
        return jwtService.generateAuthToken(request.getEmail());
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
    public GetUserResponse findUserByEmail(String email) {
        return null;
    }

    @Override
    public void deleteUser(Long id) {

    }
}
