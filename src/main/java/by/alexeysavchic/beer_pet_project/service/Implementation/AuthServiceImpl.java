package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtDTO;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.ErrorMessages;
import by.alexeysavchic.beer_pet_project.exception.RefreshTokenIsAbsentException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpHeaders;
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

    private static final Logger logger= LogManager.getLogger(AuthServiceImpl.class);

    @Override
    public JwtDTO signUp(@Valid UserRegisterRequest request)
    {
        User user = userMapper.userRegisterRequestToUser(request);
        userRepository.save(user);

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtDTO;
    }

    @Override
    public JwtDTO logIn(@Valid LogInRequest request)
    {
        User user=userRepository.findUserByEmail(request.getEmail()).orElseThrow(()->
                new UsernameNotFoundException("User with email" + request.getEmail() + "not found"));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
        {
            logger.error(ErrorMessages.wrongPassword);
            throw new WrongPasswordException();
        }

        JwtDTO jwtDTO = new JwtDTO();
        jwtDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtDTO;
    }

    @Override
    public String refresh(HttpServletRequest request)
    {
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken !=null && bearerToken.startsWith("Bearer "))
        {
            return jwtService.generateBaseToken(jwtService.
                    getEmailFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7)));
        }
        logger.error(ErrorMessages.absentRefreshToken);
        throw new RefreshTokenIsAbsentException();
    }
}
