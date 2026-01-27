package by.alexeysavchic.beer_pet_project.customValidations;

import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UniqueUsernameValidator implements ConstraintValidator<UniqueUsername, String> {

    UserRepository repository;

    @Override
    public boolean isValid(String username, ConstraintValidatorContext constraintValidatorContext) {
        return (!repository.existsByUsername(username));
    }
}
