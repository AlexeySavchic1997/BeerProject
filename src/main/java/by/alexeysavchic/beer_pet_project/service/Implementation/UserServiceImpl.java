package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import jakarta.transaction.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService
{

    UserRepository userRepository;

    JwtService jwtService;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, JwtService jwtService, UserMapper userMapper, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.userMapper = userMapper;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void signUp(UserRegisterRequest request)
    {
        User user = userMapper.userRegisterRequestToUser(request);
        userRepository.save(user);
    }

    @Override
    @Transactional
    public String logIn(LogInRequest request)
    {
        User user=userRepository.findUserByEmail(request.getEmail()).orElseThrow(()->new RuntimeException("no user"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new RuntimeException("password missmatch");
        }

        return jwtService.generateJwtToken(request.getEmail());
    }



}
