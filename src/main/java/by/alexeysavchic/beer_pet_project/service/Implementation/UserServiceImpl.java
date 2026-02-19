package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.EmailAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.UserNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.UsernameAlreadyExistsException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    private final SecurityContextService securityContextService;

    private final PasswordEncoder passwordEncoder;

    @Override
    public void changeCredentials(@Valid ChangeCredentialsRequest request) {
        Long userId = securityContextService.getCurrentUser().getId();
        User user = userRepository.findUserById(userId).orElseThrow(() ->
                new UserNotFoundException(userId));

        if (userRepository.existsByUsername(request.getUsername()) && !user.getUsername().equals(request.getUsername())) {
            throw new UsernameAlreadyExistsException(request.getUsername());
        }

        if (userRepository.existsByEmail(request.getEmail()) && !user.getEmail().equals(request.getEmail())) {
            throw new EmailAlreadyExistsException(request.getEmail());
        }

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        } else {
            throw new WrongPasswordException(securityContextService.getCurrentUser().getEmail());
        }
        userRepository.save(user);
    }
}
