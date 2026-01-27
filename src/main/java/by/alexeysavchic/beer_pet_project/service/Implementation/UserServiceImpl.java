package by.alexeysavchic.beer_pet_project.service.Implementation;

import by.alexeysavchic.beer_pet_project.dto.request.ChangeCredentialsRequest;
import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.exception.UserNotFoundException;
import by.alexeysavchic.beer_pet_project.exception.WrongPasswordException;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import by.alexeysavchic.beer_pet_project.security.SecurityContextService;
import by.alexeysavchic.beer_pet_project.service.Interface.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService
{
    UserRepository userRepository;

    SecurityContextService securityContextService;

    PasswordEncoder passwordEncoder;

    @Override
    public void changeCredentials(ChangeCredentialsRequest request)
    {
        User user = userRepository.findUserById(securityContextService.getCurrentUser().getId()).orElseThrow(()->
                new UserNotFoundException());

        user.setUsername(request.getUsername());
        user.setEmail(request.getEmail());

        if (passwordEncoder.matches(request.getOldPassword(), user.getPassword()))
        {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        }
        else
        {
            throw new WrongPasswordException();
        }
        userRepository.save(user);
    }
}
