package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtDTO;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService
{
    UserRepository userRepository;

    JwtService jwtService;

    UserMapper userMapper;

    PasswordEncoder passwordEncoder;

    @Override
    public JwtDTO signUp(UserRegisterRequest request)
    {
        User user = userMapper.userRegisterRequestToUser(request);
        userRepository.save(user);

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtDTO;
    }

    @Override
    public JwtDTO logIn(LogInRequest request)
    {
        User user=userRepository.findUserByEmail(request.getEmail()).orElseThrow(()->
                new UsernameNotFoundException("User with email" + request.getEmail() + "not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new WrongPasswordException();
        }

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtDTO;
    }

    @Override
    public String refresh(String token)
    {
        return jwtService.generateBaseToken(jwtService.getEmailFromToken(token));
    }



}
