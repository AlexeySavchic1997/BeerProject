package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.LogInRequest;
import by.alexeysavchic.beer_pet_project.dto.request.UserRegisterRequest;
import by.alexeysavchic.beer_pet_project.dto.response.JwtResponseDTO;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.EmailAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.RefreshTokenIsAbsentException;
import by.alexeysavchic.beer_pet_project.exception.UsernameAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.mapper.UserMapper;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.jwt.JwtService;
import by.alexeysavchic.beer_pet_project.service.Interface.AuthService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


@Service
@AllArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;

    private final JwtService jwtService;

    private final UserMapper userMapper;

    private final PasswordEncoder passwordEncoder;

    @Override
    public JwtResponseDTO signUp(@Valid UserRegisterRequest request) {
        User user = userMapper.userRegisterRequestToUser(request);

        if (userRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        userRepository.save(user);

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtResponseDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtResponseDTO;
    }

    @Override
    public JwtResponseDTO logIn(@Valid LogInRequest request) {
        User user = userRepository.findUserByEmail(request.getEmail()).orElseThrow(() ->
                new UsernameNotFoundException("User with email" + request.getEmail() + "not found"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new WrongPasswordException(request.getEmail());
        }

        JwtResponseDTO jwtResponseDTO = new JwtResponseDTO();
        jwtResponseDTO.setBaseToken(jwtService.generateBaseToken(request.getEmail()));
        jwtResponseDTO.setRefreshToken(jwtService.generateRefreshToken(request.getEmail()));

        return jwtResponseDTO;
    }

    @Override
    public JwtResponseDTO refresh(HttpServletRequest request) {
        JwtResponseDTO response = new JwtResponseDTO();
        String bearerToken = request.getHeader(HttpHeaders.AUTHORIZATION);
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            response.setBaseToken(jwtService.generateBaseToken(jwtService.
                    getEmailFromToken(request.getHeader(HttpHeaders.AUTHORIZATION).substring(7))));
            response.setRefreshToken(bearerToken.substring(7));
            return response;
        }
        throw new RefreshTokenIsAbsentException();
    }
}
