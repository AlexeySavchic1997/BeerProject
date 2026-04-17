package by.alexeysavchic.beer_pet_project.exception;

import java.util.List;

public class BeersNotFoundException extends RuntimeException
{
    public BeersNotFoundException(List<Long> ids) {
        super(String.format(ErrorMessages.beersNotFoundException, ids));
    }
}
