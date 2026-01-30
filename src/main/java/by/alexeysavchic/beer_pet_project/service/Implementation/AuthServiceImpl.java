package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.CookieDTO;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseCookie;
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
    public CookieDTO signUp(@Valid UserRegisterRequest request)
    {
        User user = userMapper.userRegisterRequestToUser(request);
        userRepository.save(user);

        CookieDTO cookieDTO = new CookieDTO();
        cookieDTO.setBaseCookie(jwtService.createBaseCookie(request.getEmail()));
        cookieDTO.setRefreshCookie(jwtService.createRefreshCookie(request.getEmail()));

        return cookieDTO;
    }

    @Override
    public CookieDTO logIn(@Valid LogInRequest request)
    {
        User user=userRepository.findUserByEmail(request.getEmail()).orElseThrow(()->
                new UsernameNotFoundException("User with email" + request.getEmail() + "not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            throw new WrongPasswordException();
        }

        CookieDTO cookieDTO = new CookieDTO();
        cookieDTO.setBaseCookie(jwtService.createBaseCookie(request.getEmail()));
        cookieDTO.setRefreshCookie(jwtService.createRefreshCookie(request.getEmail()));

        return cookieDTO;
    }

    @Override
    public ResponseCookie refresh(String token)
    {
        return jwtService.createBaseCookie(jwtService.getEmailFromToken(token));
    }
}
