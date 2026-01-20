package by.alexeysavchic.beer_pet_project.security;

import by.alexeysavchic.beer_pet_project.entity.User;
import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class CustomUserDetailsService implements UserDetailsService
{
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findUserByUsername(username).orElseThrow(()->
                new UsernameNotFoundException("User not found with username: " + username));

        return new CustomUserDetails(user);
    }
}
