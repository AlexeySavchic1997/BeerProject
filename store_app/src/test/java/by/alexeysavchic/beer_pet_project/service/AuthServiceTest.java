package by.alexeysavchic.beer_pet_project.service;

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
import by.alexeysavchic.beer_pet_project.service.Implementation.AuthServiceImpl;
import jakarta.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private JwtService jwtService;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @Nested
    class signUpTests {
        @Test
        void successfulSignUpTest() {

            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername("testUser");
            request.setEmail("test@gmail.com");

            User mappedUser = new User();

            when(userMapper.userRegisterRequestToUser(request)).thenReturn(mappedUser);
            when(userRepository.existsByUsername("testUser")).thenReturn(false);
            when(userRepository.existsByEmail("test@gmail.com")).thenReturn(false);
            when(jwtService.generateBaseToken("test@gmail.com")).thenReturn("base_token");
            when(jwtService.generateRefreshToken("test@gmail.com")).thenReturn("refresh_token");

            JwtResponseDTO response = authService.signUp(request);

            assertNotNull(response);
            assertEquals("base_token", response.getBaseToken());
            assertEquals("refresh_token", response.getRefreshToken());

            verify(userRepository, times(1)).save(mappedUser);
        }

        @Test
        void throwsExceptionWhenUsernameAlreadyExists() {
            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername("testUser");

            when(userMapper.userRegisterRequestToUser(request)).thenReturn(new User());
            when(userRepository.existsByUsername("testUser")).thenReturn(true);

            assertThrows(UsernameAlreadyExistsException.class, () -> authService.signUp(request));

            verify(userRepository, never()).save(any());
        }

        @Test
        void throwsExceptionWhenEmailAlreadyExists() {
            UserRegisterRequest request = new UserRegisterRequest();
            request.setUsername("testUser");
            request.setEmail("test@gmail.com");

            when(userMapper.userRegisterRequestToUser(request)).thenReturn(new User());
            when(userRepository.existsByUsername("testUser")).thenReturn(false);
            when(userRepository.existsByEmail("test@gmail.com")).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> authService.signUp(request));

            verify(userRepository, never()).save(any());
        }
    }

    @Nested
    class logInTests {
        @Test
        void successfulLogInTest() {
            LogInRequest request = new LogInRequest();
            request.setEmail("test@gmail.com");
            request.setPassword("password123");

            User user = new User();
            user.setPassword("encoded_password");

            when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("password123", "encoded_password")).thenReturn(true);
            when(jwtService.generateBaseToken("test@gmail.com")).thenReturn("base_token");
            when(jwtService.generateRefreshToken("test@gmail.com")).thenReturn("refresh_token");

            JwtResponseDTO response = authService.logIn(request);

            assertNotNull(response);
            assertEquals("base_token", response.getBaseToken());
            assertEquals("refresh_token", response.getRefreshToken());
        }

        @Test
        void throwsExceptionWhenUserNotFound() {
            LogInRequest request = new LogInRequest();
            request.setEmail("notfound@gmail.com");

            when(userRepository.findUserByEmail("notfound@gmail.com")).thenReturn(Optional.empty());

            assertThrows(UsernameNotFoundException.class, () -> authService.logIn(request));

            verify(passwordEncoder, never()).matches(any(), any());
        }

        @Test
        void throwsExceptionWhenPasswordIsWrong() {
            LogInRequest request = new LogInRequest();
            request.setEmail("test@gmail.com");
            request.setPassword("wrong_password");

            User user = new User();
            user.setPassword("encoded_password");

            when(userRepository.findUserByEmail("test@gmail.com")).thenReturn(Optional.of(user));
            when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);

            assertThrows(WrongPasswordException.class, () -> authService.logIn(request));

            verify(jwtService, never()).generateBaseToken(any());
        }
    }

    @Nested
    class refreshTests {
        @Test
        void successfulRefreshTest() {
            HttpServletRequest request = mock(HttpServletRequest.class);

            String validHeader = "Bearer valid_refresh_token_string";
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(validHeader);
            when(jwtService.getEmailFromToken("valid_refresh_token_string")).thenReturn("test@gmail.com");
            when(jwtService.generateBaseToken("test@gmail.com")).thenReturn("new_base_token");

            JwtResponseDTO response = authService.refresh(request);

            assertNotNull(response);
            assertEquals("new_base_token", response.getBaseToken());
            assertEquals("valid_refresh_token_string", response.getRefreshToken());
        }

        @Test
        void throwsExceptionWhenHeaderIsAbsent() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn(null);

            assertThrows(RefreshTokenIsAbsentException.class, () -> authService.refresh(request));
        }

        @Test
        void throwsExceptionWhenHeaderHasWrongFormat() {
            HttpServletRequest request = mock(HttpServletRequest.class);
            when(request.getHeader(HttpHeaders.AUTHORIZATION)).thenReturn("invalid_format_token");

            assertThrows(RefreshTokenIsAbsentException.class, () -> authService.refresh(request));
        }
    }
}