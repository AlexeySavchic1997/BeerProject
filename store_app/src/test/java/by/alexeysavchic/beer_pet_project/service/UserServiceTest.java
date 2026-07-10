package by.alexeysavchic.beer_pet_project.service;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.EmailAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.UserNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.UsernameAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Implementation.UserServiceImpl;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private SecurityContextService securityContextService;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Nested
    class changeCredentialsTests {

        @Test
        void successfulChangeCredentialsTest() {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest();
            request.setUsername("newUsername");
            request.setEmail("newEmail@gmail.com");
            request.setOldPassword("oldPass");
            request.setNewPassword("newPass");

            User contextUser = new User();
            contextUser.setId(1L);
            contextUser.setEmail("oldEmail@gmail.com");

            User dbUser = new User();
            dbUser.setId(1L);
            dbUser.setUsername("oldUsername");
            dbUser.setEmail("oldEmail@gmail.com");
            dbUser.setPassword("encodedOldPass");

            when(securityContextService.getCurrentUser()).thenReturn(contextUser);
            when(userRepository.findUserById(1L)).thenReturn(Optional.of(dbUser));

            when(userRepository.existsByUsername("newUsername")).thenReturn(false);
            when(userRepository.existsByEmail("newEmail@gmail.com")).thenReturn(false);

            when(passwordEncoder.matches("oldPass", "encodedOldPass")).thenReturn(true);
            when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");

            userService.changeCredentials(request);

            assertEquals("newUsername", dbUser.getUsername());
            assertEquals("newEmail@gmail.com", dbUser.getEmail());
            assertEquals("encodedNewPass", dbUser.getPassword());

            verify(userRepository, times(1)).save(dbUser);
        }

        @Test
        void throwsExceptionWhenUserNotFound() {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest();
            User contextUser = new User();
            contextUser.setId(99L);

            when(securityContextService.getCurrentUser()).thenReturn(contextUser);
            when(userRepository.findUserById(99L)).thenReturn(Optional.empty());

            assertThrows(UserNotFoundException.class, () -> userService.changeCredentials(request));

            verify(userRepository, never()).save(any());
        }

        @Test
        void throwsExceptionWhenUsernameAlreadyExists() {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest();
            request.setUsername("takenUsername");
            request.setEmail("email@gmail.com");

            User contextUser = new User();
            contextUser.setId(1L);

            User dbUser = new User();
            dbUser.setId(1L);
            dbUser.setUsername("oldUsername");

            when(securityContextService.getCurrentUser()).thenReturn(contextUser);
            when(userRepository.findUserById(1L)).thenReturn(Optional.of(dbUser));

            when(userRepository.existsByUsername("takenUsername")).thenReturn(true);

            assertThrows(UsernameAlreadyExistsException.class, () -> userService.changeCredentials(request));

            verify(userRepository, never()).save(any());
        }

        @Test
        void throwsExceptionWhenEmailAlreadyExists() {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest();
            request.setUsername("sameUsername");
            request.setEmail("takenEmail@gmail.com");

            User contextUser = new User();
            contextUser.setId(1L);

            User dbUser = new User();
            dbUser.setId(1L);
            dbUser.setUsername("sameUsername");
            dbUser.setEmail("oldEmail@gmail.com");

            when(securityContextService.getCurrentUser()).thenReturn(contextUser);
            when(userRepository.findUserById(1L)).thenReturn(Optional.of(dbUser));

            // Имитируем, что username остался прежним
            when(userRepository.existsByUsername("sameUsername")).thenReturn(true);
            // Имитируем, что новый email занят
            when(userRepository.existsByEmail("takenEmail@gmail.com")).thenReturn(true);

            assertThrows(EmailAlreadyExistsException.class, () -> userService.changeCredentials(request));

            verify(userRepository, never()).save(any());
        }

        @Test
        void throwsExceptionWhenOldPasswordIsWrong() {
            ChangeCredentialsRequest request = new ChangeCredentialsRequest();
            request.setUsername("sameUsername");
            request.setEmail("sameEmail@gmail.com");
            request.setOldPassword("wrongPass");

            User contextUser = new User();
            contextUser.setId(1L);
            contextUser.setEmail("sameEmail@gmail.com");

            User dbUser = new User();
            dbUser.setId(1L);
            dbUser.setUsername("sameUsername");
            dbUser.setEmail("sameEmail@gmail.com");
            dbUser.setPassword("encodedOldPass");

            when(securityContextService.getCurrentUser()).thenReturn(contextUser);
            when(userRepository.findUserById(1L)).thenReturn(Optional.of(dbUser));

            when(userRepository.existsByUsername("sameUsername")).thenReturn(true);
            when(userRepository.existsByEmail("sameEmail@gmail.com")).thenReturn(true);

            when(passwordEncoder.matches("wrongPass", "encodedOldPass")).thenReturn(false);

            assertThrows(WrongPasswordException.class, () -> userService.changeCredentials(request));

            verify(userRepository, never()).save(any());
            verify(passwordEncoder, never()).encode(any());
        }
    }
}
