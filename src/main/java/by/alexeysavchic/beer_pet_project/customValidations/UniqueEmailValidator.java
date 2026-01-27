package by.alexeysavchic.beer_pet_project.customValidations;

import by.alexeysavchic.beer_pet_project.repository.UserRepository;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    UserRepository repository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        return (!repository.existsByEmail(email));
    }
}
