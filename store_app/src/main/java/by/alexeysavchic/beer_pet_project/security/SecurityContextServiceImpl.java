package by.alexeysavchic.beer_pet_project.security;

import by.alexeysavchic.beer_pet_project.entity.User;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class SecurityContextServiceImpl implements SecurityContextService {

    @Override
    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }
        Object principal = authentication.getPrincipal();
        if (principal instanceof CustomUserDetails) {
            return ((CustomUserDetails) principal).getUser();
        }
        return null;
    }
}
